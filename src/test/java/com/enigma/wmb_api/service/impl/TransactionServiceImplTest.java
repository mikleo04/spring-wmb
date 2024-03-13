package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.TransactionStatus;
import com.enigma.wmb_api.constant.TransactionType;
import com.enigma.wmb_api.dto.request.SearchTransactionRequest;
import com.enigma.wmb_api.dto.request.TransactionDetailRequest;
import com.enigma.wmb_api.dto.request.TransactionRequest;
import com.enigma.wmb_api.dto.request.UpdateStatusTransactionRequest;
import com.enigma.wmb_api.dto.response.MenuResponse;
import com.enigma.wmb_api.dto.response.TableResponse;
import com.enigma.wmb_api.dto.response.TransTypeResponse;
import com.enigma.wmb_api.dto.response.TransactionResponse;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.repository.TransactionRepository;
import com.enigma.wmb_api.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private TableService tableService;
    @Mock
    private TransTypeService transTypeService;
    @Mock
    private TransactionDetailService transactionDetailService;
    @Mock
    private MenuService menuService;
    @Mock
    private PaymentService paymentService;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl(
                transactionRepository,
                customerService,
                tableService,
                transTypeService,
                transactionDetailService,
                menuService,
                paymentService
        );
    }

    @Test
    void shouldReturnTransactionResponseWhenCreate() {
        //Given
        List<TransactionDetailRequest> detailRequests = List.of(
                TransactionDetailRequest.builder()
                        .menuId("Menu-01")
                        .quantity(5)
                        .build(),
                TransactionDetailRequest.builder()
                        .menuId("Menu-02")
                        .quantity(3)
                        .build()
        );

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .customerId("Customer-01")
                .tableId("Table-01")
                .transTypeid(TransactionType.EI)
                .detailRequests(detailRequests)
                .build();

        Customer customer = Customer.builder()
                .id("Customer-01")
                .name("Customer")
                .mobilePhoneNumber("0812873409012")
                .userAccount(UserAccount.builder().build())
                .isMember(true)
                .build();

        Transaction transaction = Transaction.builder()
                .id("Transaction-01")
                .table(DiningTable.builder().build())
                .transDate(new Date())
                .customer(customer)
                .transType(TransType.builder().build())
                .build();

        Menu menu = Menu.builder()
                .id("Menu-01")
                .name("menu")
                .price(12000L)
                .status(true)
                .images(List.of(Image.builder().build()))
                .build();

        List<TransactionDetail> transactionDetails = List.of(
            TransactionDetail.builder()
                    .id("TransactionDetail-01")
                    .quantity(10)
                    .menu(menu)
                    .menuPrice(12000L)
                    .transaction(transaction)
                    .build(),
                TransactionDetail.builder()
                        .id("TransactionDetail-02")
                        .quantity(10)
                        .menu(menu)
                        .menuPrice(12000L)
                        .transaction(transaction)
                        .build()
        );
        transaction.setTransactionDetails(transactionDetails);

        Payment payment = Payment.builder()
                .id("Payment-01")
                .transactionStatus(TransactionStatus.ORDERED)
                .token("123456789")
                .transaction(transaction)
                .redirectUrl("https://8080")
                .build();

        //Stubbing
        Mockito.when(customerService.getById(transactionRequest.getCustomerId()))
                .thenReturn(customer);

        Mockito.when(tableService.getById(transactionRequest.getTableId()))
                .thenReturn(TableResponse.builder().build());

        Mockito.when(transTypeService.getById(transactionRequest.getTransTypeid()))
                .thenReturn(TransTypeResponse.builder().build());

        Mockito.when(transactionRepository.saveAndFlush(Mockito.any()))
                .thenReturn(transaction);

        Mockito.when(menuService.getOneById(Mockito.any(String.class)))
                .thenReturn(MenuResponse.builder().build());

        Mockito.when(transactionDetailService.createBulk(Mockito.any()))
                .thenReturn(transactionDetails);

        Mockito.when(paymentService.createPayment(Mockito.any()))
                .thenReturn(payment);
        //When
        TransactionResponse actualTransactionResponse = transactionService.create(transactionRequest);

        //Then
        assertNotNull(actualTransactionResponse);
        assertEquals(transactionRequest.getCustomerId(), actualTransactionResponse.getCustomerId());
    }

    @Test
    void shouldReturnListTransactionWhenGetAll() {
        //Given
        SearchTransactionRequest transactionRequest = SearchTransactionRequest.builder()
                .startDate("2024-03-10")
                .endDate("2024-03-12")
                .size(10)
                .page(1)
                .transactionStatus("ORDERED")
                .direction("ASC")
                .sortBy("transDate")
                .transactionTypeId("TA")
                .date("2024-03-11")
                .customerId("Customer-01")
                .build();

        Payment payment = Payment.builder()
                .id("Payment-01")
                .transactionStatus(TransactionStatus.ORDERED)
                .token("123456789")
                .transaction(Transaction.builder().build())
                .redirectUrl("https://8080")
                .build();
        Menu menu = Menu.builder()
                .id("Menu-01")
                .name("menu")
                .price(12000L)
                .status(true)
                .images(List.of(Image.builder().build()))
                .build();
        Customer customer = Customer.builder()
                .id("Customer-01")
                .name("Customer")
                .mobilePhoneNumber("0812873409012")
                .userAccount(UserAccount.builder().build())
                .isMember(true)
                .build();
        TransType transType = TransType.builder()
                .id(TransactionType.TA)
                .description("Take Away")
                .build();
        DiningTable table = DiningTable.builder()
                .id("Table-01")
                .name("T01")
                .build();

        List<TransactionDetail> transactionDetails = List.of(
                TransactionDetail.builder()
                        .id("TransDetail-01")
                        .menuPrice(12000L)
                        .menu(menu)
                        .quantity(12)
                        .transaction(Transaction.builder().build())
                        .build()
        );

        List<Transaction> transactionList = List.of(
                Transaction.builder()
                        .id("Transaction-01")
                        .table(table)
                        .transDate(new Date())
                        .customer(customer)
                        .transType(transType)
                        .payment(payment)
                        .transactionDetails(transactionDetails)
                        .build(),
                Transaction.builder()
                        .id("Transaction-02")
                        .table(DiningTable.builder().build())
                        .transDate(new Date())
                        .customer(Customer.builder().build())
                        .transType(TransType.builder().build())
                        .payment(payment)
                        .transactionDetails(transactionDetails)
                        .build()
        );

        Page<Transaction> transactions = new PageImpl<>(transactionList);

        Mockito.when(transactionRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(transactions);

        //When
        Page<TransactionResponse> actualTransactionResponses = transactionService.getAll(transactionRequest);

        //Then
        assertNotNull(actualTransactionResponses);
        assertEquals(transactions.getSize(), actualTransactionResponses.getSize());
    }

    @Test
    void shouldReturnTransactionResponseWhenGetById() {
        String id = "Transaction-01";

        Payment payment = Payment.builder()
                .id("Payment-01")
                .transactionStatus(TransactionStatus.ORDERED)
                .token("123456789")
                .transaction(Transaction.builder().build())
                .redirectUrl("https://8080")
                .build();
        Menu menu = Menu.builder()
                .id("Menu-01")
                .name("menu")
                .price(12000L)
                .status(true)
                .images(List.of(Image.builder().build()))
                .build();
        Customer customer = Customer.builder()
                .id("Customer-01")
                .name("Customer")
                .mobilePhoneNumber("0812873409012")
                .userAccount(UserAccount.builder().build())
                .isMember(true)
                .build();
        TransType transType = TransType.builder()
                .id(TransactionType.TA)
                .description("Take Away")
                .build();
        DiningTable table = DiningTable.builder()
                .id("Table-01")
                .name("T01")
                .build();

        List<TransactionDetail> transactionDetails = List.of(
                TransactionDetail.builder()
                        .id("TransDetail-01")
                        .menuPrice(12000L)
                        .menu(menu)
                        .quantity(12)
                        .transaction(Transaction.builder().build())
                        .build()
        );

        Transaction transaction = Transaction.builder()
                .id("Transaction-01")
                .table(table)
                .transDate(new Date())
                .customer(customer)
                .transType(transType)
                .payment(payment)
                .transactionDetails(transactionDetails)
                .build();

        //Stubbing
        Mockito.when(transactionRepository.findById(id))
                .thenReturn(Optional.ofNullable(transaction));

        //When
        TransactionResponse actualTransactionResponse = transactionService.getById(id);

        //Then
        assertNotNull(actualTransactionResponse);
        assertEquals(id, actualTransactionResponse.getId());
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenNotFound() {
        String id = "Transction-01";

        //Stubbing
        Mockito.when(transactionRepository.findById(id))
                .thenThrow(ResponseStatusException.class);

        //When//Then
        assertThrows(ResponseStatusException.class, () -> {
            transactionService.getById(id);
        });
    }

    @Test
    void shouldSetTransactionStatusOnce() {
        //Give
        UpdateStatusTransactionRequest statusTransactionRequest = UpdateStatusTransactionRequest.builder()
                .orderId("Order-01")
                .transactionStatus(TransactionStatus.SETTLEMENT)
                .build();

        Payment payment = Payment.builder()
                .id("Payment-01")
                .transactionStatus(TransactionStatus.ORDERED)
                .token("123456789")
                .transaction(Transaction.builder().build())
                .redirectUrl("https://8080")
                .build();

        Transaction transaction = Transaction.builder()
                .id("Transaction-01")
                .table(DiningTable.builder().build())
                .transDate(new Date())
                .customer(Customer.builder().build())
                .transType(TransType.builder().build())
                .payment(payment)
                .transactionDetails(List.of(TransactionDetail.builder().build()))
                .build();

        Mockito.when(transactionRepository.findById(statusTransactionRequest.getOrderId()))
                .thenReturn(Optional.ofNullable(transaction));

        Payment returnPayment = transaction.getPayment();

        returnPayment.setTransactionStatus(statusTransactionRequest.getTransactionStatus());

        //When
        transactionService.updateStatus(statusTransactionRequest);

        //Then
        Mockito.verify(transactionRepository, Mockito.times(1)).findById(statusTransactionRequest.getOrderId());
        assertEquals(statusTransactionRequest.getTransactionStatus(), transaction.getPayment().getTransactionStatus());
    }

    @Test
    void getReportCsv() {
    }

    @Test
    void getReportPdf() {
    }
}