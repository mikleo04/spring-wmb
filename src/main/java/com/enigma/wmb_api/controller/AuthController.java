package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.LoginRequest;
import com.enigma.wmb_api.dto.request.RegisterRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.LoginResponse;
import com.enigma.wmb_api.dto.response.RegisterResponse;
import com.enigma.wmb_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.AUTH_API)
public class AuthController {

    private final AuthService service;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        RegisterResponse registerResult = service.register(request);

        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Success cretae user")
                .data(registerResult)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(
            path = "/register/admin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<RegisterResponse>> registerAdmid(@RequestBody RegisterRequest request) {
        RegisterResponse registerResult = service.registerAdmin(request);

        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Succes create user admin")
                .data(registerResult)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> login(@RequestBody LoginRequest request) {
        LoginResponse loginResult = service.login(request);

        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login success")
                .data(loginResult)
                .build();

        return ResponseEntity.ok(response);
    }

}
