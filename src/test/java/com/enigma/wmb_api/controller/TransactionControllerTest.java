package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.TransactionStatus;
import com.enigma.wmb_api.constant.TransactionType;
import com.enigma.wmb_api.dto.request.SearchTransactionRequest;
import com.enigma.wmb_api.dto.request.TransactionDetailRequest;
import com.enigma.wmb_api.dto.request.TransactionRequest;
import com.enigma.wmb_api.dto.request.UpdateStatusTransactionRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PaymentResponse;
import com.enigma.wmb_api.dto.response.TransactionDetailResponse;
import com.enigma.wmb_api.dto.response.TransactionResponse;
import com.enigma.wmb_api.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave201StatusAndReturnCommonResponseWhenCreat() throws Exception {

        List<TransactionDetailRequest> detailRequests = List.of(
                TransactionDetailRequest.builder()
                        .menuId("Menu-01")
                        .quantity(2)
                        .build(),
                TransactionDetailRequest.builder()
                        .menuId("Menu-02")
                        .quantity(4)
                        .build()
        );

        TransactionRequest payload = TransactionRequest.builder()
                .tableId("T01")
                .transTypeid(TransactionType.EI)
                .detailRequests(detailRequests)
                .customerId("Customer-01")
                .build();

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .id("Transaction-01")
                .transDate(new Date())
                .detailTransaction(List.of(TransactionDetailResponse.builder().build()))
                .payment(PaymentResponse.builder().build())
                .transTypeId(TransactionType.TA)
                .tableId("Table-01")
                .customerId("Customer-01")
                .build();

        Mockito.when(transactionService.create(payload))
                .thenReturn(transactionResponse);

        //When Then
        String payloadJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<TransactionResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(201, response.getStatusCode());
                    assertEquals("Success create transaction", response.getMessage());
                });

    }

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWhenGetAll() throws Exception {

        SearchTransactionRequest transactionRequest = SearchTransactionRequest.builder()
                .page(1)
                .size(10)
                .sortBy("transDate")
                .direction("ASC")
                .build();

        List<TransactionResponse> responseList = List.of(
                TransactionResponse.builder()
                        .id("Transaction-01")
                        .transDate(new Date())
                        .detailTransaction(List.of(TransactionDetailResponse.builder().build()))
                        .payment(PaymentResponse.builder().build())
                        .transTypeId(TransactionType.TA)
                        .tableId("Table-01")
                        .customerId("Customer-01")
                        .build(),
                TransactionResponse.builder()
                        .id("Transaction-02")
                        .transDate(new Date())
                        .detailTransaction(List.of(TransactionDetailResponse.builder().build()))
                        .payment(PaymentResponse.builder().build())
                        .transTypeId(TransactionType.TA)
                        .tableId("Table-01")
                        .customerId("Customer-01")
                        .build()
        );

        Pageable pageable = PageRequest.of(transactionRequest.getPage(), transactionRequest.getSize(), Sort.by(Sort.Direction.ASC, transactionRequest.getSortBy()));
        Page<TransactionResponse> responsePage = new PageImpl<>(responseList, pageable, responseList.size());

        Mockito.when(transactionService.getAll(Mockito.any(SearchTransactionRequest.class)))
                .thenReturn(responsePage);

        //When Then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/transactions")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<TransactionResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Success get all transactions", response.getMessage());
                });

    }

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWhenGetById() throws Exception {
        //Given
        String id = "Transaction-01";
        TransactionResponse transactionResponse = TransactionResponse.builder()
                .id("Transaction-02")
                .transDate(new Date())
                .detailTransaction(List.of(TransactionDetailResponse.builder().build()))
                .payment(PaymentResponse.builder().build())
                .transTypeId(TransactionType.TA)
                .tableId("Table-01")
                .customerId("Customer-01")
                .build();

        Mockito.when(transactionService.getById(id))
                .thenReturn(transactionResponse);

        //When
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/transactions/{id}", id)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<TransactionResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Success get transaction", response.getMessage());
                });

    }

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void updateStatusTrshouldHave200StatusAndReturnCommonResponseWhenUpdateStatus() throws Exception {
        //Given
        Map<String, Object> request = Map.of(
                "order_id", "Trancation-01",
                "transaction_status", "SETTLEMENT"
        );
        UpdateStatusTransactionRequest payload = UpdateStatusTransactionRequest.builder()
                .orderId("Transaction-01")
                .transactionStatus(TransactionStatus.SETTLEMENT)
                .build();

        Mockito.doNothing().when(transactionService).updateStatus(payload);

        //When Then
        String jsonPayload = objectMapper.writeValueAsString(request);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transactions/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Success update status transaction", response.getMessage());
                });
    }

    @Test
    @Disabled
    void donwloadReportTransactionCsv() {
    }

    @Test
    @Disabled
    void donwloadReportTransactionPdf() {
    }
}