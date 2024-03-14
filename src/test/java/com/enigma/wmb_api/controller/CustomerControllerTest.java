package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "username", roles = "ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWhenGetById() throws Exception {
        //Given
        String id = "Customer-01";

        CustomerResponse mockResponse = CustomerResponse.builder()
                .id("Customer-01")
                .name("Customer")
                .userAccountId("UserAccount-01")
                .isMember(true)
                .mobilePhoneNumber("0821346523467")
                .build();

        //Stubbing
        Mockito.when(customerService.getOneById(id))
                .thenReturn(mockResponse);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/customers/{id}", id)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Success get customer", response.getMessage());
                });
    }

    @Test
    @WithMockUser(username = "username", roles = "ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWhenGetAll() throws Exception {
        SearchCustomerRequest customerRequest = SearchCustomerRequest.builder()
                .sortBy("name")
                .direction("ASC")
                .size(10)
                .page(1)
                .isMember(true)
                .name("Customer")
                .build();

        List<CustomerResponse> listResponse = List.of(
                CustomerResponse.builder()
                        .id("Customer-01")
                        .name("Customer")
                        .mobilePhoneNumber("081234569823")
                        .userAccountId("UserAccount-01")
                        .isMember(true)
                        .build(),
                CustomerResponse.builder()
                        .id("Customer-01")
                        .name("Customer")
                        .mobilePhoneNumber("081234569823")
                        .userAccountId("UserAccount-01")
                        .isMember(true)
                        .build()
        );

        Pageable pageable = PageRequest.of(customerRequest.getPage()-1, customerRequest.getSize(), Sort.by(Sort.Direction.ASC, customerRequest.getSortBy()));

        Page<CustomerResponse> mockResponse = new PageImpl<>(listResponse, pageable, listResponse.size());

        Mockito.when(customerService.getAll(Mockito.any(SearchCustomerRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/customers")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<CustomerResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Success get all customer", response.getMessage());
                });
    }

    @Test
    @WithMockUser(username = "username", roles = "ADMIN")
    void shouldHave202StatusAndReturnCommonResponseWhenUpdate() throws Exception {
        CustomerRequest payload = CustomerRequest.builder()
                .id("Customer-01")
                .name("Customer")
                .isMember(true)
                .mobilePhoneNumber("08217634092")
                .build();

        CustomerResponse mockCustomer = CustomerResponse.builder()
                .id("Customer-01")
                .name("Customer")
                .isMember(true)
                .mobilePhoneNumber("08127634982")
                .userAccountId("UserAccount-01")
                .build();

        Mockito.when(customerService.update(payload))
                .thenReturn(mockCustomer);

        //When Then
        String jsonPayload = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload)
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                   CommonResponse<CustomerResponse> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(202, response.getStatusCode());
                    assertEquals("Success update customer", response.getMessage());
                });
    }

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave202StatusAndReturnCommonResponseWhenDelete() throws Exception {
        String id = "Customer-01";
        Mockito.doNothing().when(customerService).delete(id);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/customers/{id}", id)
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(202, response.getStatusCode());
                    assertEquals("Success delete customer", response.getMessage());
                });
    }
}