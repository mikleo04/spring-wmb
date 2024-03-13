package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.entity.TransactionDetail;
import com.enigma.wmb_api.repository.TransactionDetailRepository;
import com.enigma.wmb_api.service.TransactionDetailService;
import com.enigma.wmb_api.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransaactionDetailServiceImplTest {

    @Mock
    private TransactionDetailRepository transactionDetailRepository;
    @Mock
    private ValidationUtil validationUtil;
    private TransactionDetailService transactionDetailService;

    @BeforeEach
    void setup() {
        transactionDetailService = new TransaactionDetailServiceImpl(transactionDetailRepository, validationUtil);
    }


    @Test
    void shouldReturnListTransactionDetail() {
        //Given
        List<TransactionDetail> transactionDetails = List.of(
                TransactionDetail.builder()
                        .id("TransactionDetail-01")
                        .quantity(10)
                        .menu(Menu.builder().build())
                        .menuPrice(12000L)
                        .transaction(Transaction.builder().build())
                        .build(),
                TransactionDetail.builder()
                        .id("TransactionDetail-02")
                        .quantity(10)
                        .menu(Menu.builder().build())
                        .menuPrice(12000L)
                        .transaction(Transaction.builder().build())
                        .build()
        );

        //Stubbing
        validationUtil.validate(transactionDetails);
        Mockito.when(transactionDetailRepository.saveAllAndFlush(transactionDetails))
                .thenReturn(transactionDetails);

        //When
        List<TransactionDetail> actualTransactionDetailList = transactionDetailService.createBulk(transactionDetails);

        //Then
        assertEquals(transactionDetails.size(), actualTransactionDetailList.size());
        Mockito.verify(transactionDetailRepository, Mockito.times(1)).saveAllAndFlush(transactionDetails);
    }
}