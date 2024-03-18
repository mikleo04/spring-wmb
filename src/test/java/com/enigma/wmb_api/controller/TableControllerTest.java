package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.SearchTableRequest;
import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.TableResponse;
import com.enigma.wmb_api.service.TableService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
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
class TableControllerTest {

    @MockBean
    private TableService tableService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave201StatusAndReturnCommonResponseWhenCreat() throws Exception {
        //Given
        TableRequest payload = TableRequest.builder()
                .id("Table-01")
                .name("T01")
                .build();

        TableResponse tableResponse = TableResponse.builder()
                .id("Table-01")
                .name("T01")
                .build();

        Mockito.when(tableService.creat(payload))
                .thenReturn(tableResponse);

        String jsonPayload = objectMapper.writeValueAsString(payload);

        //When Then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload)
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<TableResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(201, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_SAVE_DATA, response.getMessage());
                });
    }

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWhenGetById() throws Exception {
        //Given
        String id = "Table-01";
        TableResponse tableResponse = TableResponse.builder()
                .id("Table-01")
                .name("T01")
                .build();

        Mockito.when(tableService.getById(id))
                .thenReturn(tableResponse);

        //When Then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/tables/{id}", id)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<TableResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<TableResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                });
    }

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWhenGetAll() throws Exception {
        SearchTableRequest tableRequest = SearchTableRequest.builder()
                .direction("ASC")
                .page(1)
                .size(10)
                .sortBy("name")
                .build();

        List<TableResponse> listResponse = List.of(
                TableResponse.builder()
                        .id("Table-01")
                        .name("T01")
                        .build(),
                TableResponse.builder()
                        .id("Table-02")
                        .name("T02")
                        .build()
        );

        Pageable pageable = PageRequest.of(tableRequest.getPage()-1, tableRequest.getSize(), Sort.by(Sort.Direction.ASC, tableRequest.getSortBy()));
        Page<TableResponse> mockResponse = new PageImpl<>(listResponse, pageable, listResponse.size());

        Mockito.when(tableService.getAll(Mockito.any(SearchTableRequest.class)))
                .thenReturn(mockResponse);

        //When Then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/tables")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result ->  {
                    CommonResponse<List<TableResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<List<TableResponse>>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                });
    }

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWithDelete() throws Exception {
        //Given
        String id = "Table-01";
        Mockito.doNothing().when(tableService).delete(id);

        //When Then
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/tables/{id}", id)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<String>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_DELETE_DATA, response.getMessage());
                });
    }
}