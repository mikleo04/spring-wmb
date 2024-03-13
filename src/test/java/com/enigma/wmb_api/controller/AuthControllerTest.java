package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.LoginRequest;
import com.enigma.wmb_api.dto.request.RegisterRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.LoginResponse;
import com.enigma.wmb_api.dto.response.RegisterResponse;
import com.enigma.wmb_api.service.AuthService;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "username")
    void shouldHave201StatusAndReturnCommonResponseWhenReister() throws Exception {
        //Given
        RegisterRequest payload = RegisterRequest.builder()
                .name("username")
                .email("username@gmail.com")
                .password("password")
                .build();

        RegisterResponse mockResponse = RegisterResponse.builder()
                .name(payload.getName())
                .email(payload.getEmail())
                .role(List.of(UserRole.ROLE_CUSTOMER))
                .build();

        Mockito.when(authService.register(payload))
                .thenReturn(mockResponse);

        //When //Then
        String jsonPayload = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload)
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<RegisterResponse> response =objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(201, response.getStatusCode());
                    assertEquals("Success cretae user", response.getMessage());
                });
    }

    @Test
    @WithMockUser(username = "username")
    void shouldHave201StatusAndReturnCommonResponseWhenReisterAdmin() throws Exception {
        //Given
        RegisterRequest payload = RegisterRequest.builder()
                .name("admin")
                .email("admin@gmail.com")
                .password("password")
                .build();

        RegisterResponse mockResponse = RegisterResponse.builder()
                .name(payload.getName())
                .email(payload.getEmail())
                .role(List.of(UserRole.ROLE_CUSTOMER))
                .build();

        Mockito.when(authService.registerAdmin(payload))
                .thenReturn(mockResponse);

        //When //Then
        String jsonPayload = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/register/admin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPayload)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<RegisterResponse> response =objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(201, response.getStatusCode());
                    assertEquals("Succes create user admin", response.getMessage());
                });
    }

    @Test
    @WithMockUser(username = "username")
    void shouldHave200StatusAndReturnCommonResponseWhenLogin() throws Exception {
        LoginRequest payload = LoginRequest.builder()
                .email("username@gmail.com")
                .password("password")
                .build();

        LoginResponse mockResponse = LoginResponse.builder()
                .token("Token123")
                .role(List.of(UserRole.ROLE_CUSTOMER))
                .email(payload.getEmail())
                .build();

        Mockito.when(authService.login(payload))
                .thenReturn(mockResponse);

        //When Then
        String jsonPayload = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Login success", response.getMessage());
                });
    }
}