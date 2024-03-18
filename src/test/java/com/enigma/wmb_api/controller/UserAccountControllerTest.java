package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.SearchUSerAccountResquest;
import com.enigma.wmb_api.dto.request.UpdateUserAccountRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.UserAccountResponse;
import com.enigma.wmb_api.service.UserService;
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
class UserAccountControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWhenUpdateEmailOrPassword() throws Exception {
        //Given
        UpdateUserAccountRequest payload = UpdateUserAccountRequest.builder()
                .userAccountId("UserAccount-01")
                .email("username@gmail.com")
                .password("password")
                .build();

        Mockito.doNothing().when(userService).updateEmailOrPassword(payload);

        //When Then
        String jsonPayload = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<String> response =objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_UPDATE_DATA, response.getMessage());
                });
    }

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWhenGetOneById() throws Exception {
        //Given
        String id = "UserAccount-01";
        UserAccountResponse accountResponse = UserAccountResponse.builder()
                .id("UserAccount-01")
                .roles(List.of(UserRole.ROLE_CUSTOMER))
                .isEnable(true)
                .email("username@gmail.com")
                .password("password")
                .build();

        Mockito.when(userService.getOneById(id))
                .thenReturn(accountResponse);

        //When Then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account/{id}", id)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<UserAccountResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                });
    }

    @Test
    @WithMockUser(username = "username", roles = "SUPER_ADMIN")
    void shouldHave200StatusAndReturnCommonResponseWhenGetAll() throws Exception {
        //Given
        SearchUSerAccountResquest accountResquest = SearchUSerAccountResquest.builder()
                .direction("ASC")
                .size(10)
                .page(1)
                .sortBy("email")
                .build();

        List<UserAccountResponse> accountResponseList = List.of(
                UserAccountResponse.builder()
                        .id("UserAccount-01")
                        .roles(List.of(UserRole.ROLE_CUSTOMER))
                        .isEnable(true)
                        .email("username@gmail.com")
                        .password("password")
                        .build(),
                UserAccountResponse.builder()
                        .id("UserAccount-01")
                        .roles(List.of(UserRole.ROLE_CUSTOMER))
                        .isEnable(true)
                        .email("username@gmail.com")
                        .password("password")
                        .build()
        );

        Pageable pageable = PageRequest.of(accountResquest.getPage(), accountResquest.getSize(), Sort.by(Sort.Direction.ASC, accountResquest.getSortBy()));
        Page<UserAccountResponse> accountResponsePage = new PageImpl<>(accountResponseList, pageable, accountResponseList.size());

        Mockito.when(userService.getAll(Mockito.any(SearchUSerAccountResquest.class)))
                .thenReturn(accountResponsePage);

        //When Then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<UserAccountResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                });
    }
}