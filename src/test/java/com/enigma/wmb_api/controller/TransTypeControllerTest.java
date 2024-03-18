package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.constant.TransactionType;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.TransTypeResponse;
import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.service.TransTypeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class TransTypeControllerTest {

    @MockBean
    TransTypeService transTypeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWhenGetById() throws Exception {
        //Given
        TransactionType id = TransactionType.TA;
        TransTypeResponse transTypeResponse = TransTypeResponse.builder()
                .id(TransactionType.TA)
                .description("Take Away")
                .build();

        Mockito.when(transTypeService.getById(id))
                .thenReturn(transTypeResponse);

        //When Then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/trans-types/{id}", id)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result ->  {
                    CommonResponse<TransTypeResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                });
    }

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWhenGetAll() throws Exception {
        //Given

        List<TransTypeResponse> mockTransType = List.of(
                TransTypeResponse.builder()
                        .id(TransactionType.TA)
                        .description("Take Away")
                        .build(),
                TransTypeResponse.builder()
                        .id(TransactionType.TA)
                        .description("Take Away")
                        .build()
        );

        Mockito.when(transTypeService.getAll())
                .thenReturn(mockTransType);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/trans-types")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<TransTypeResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                });

    }
}