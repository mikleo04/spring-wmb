package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.LoginRequest;
import com.enigma.wmb_api.dto.request.RegisterRequest;
import com.enigma.wmb_api.dto.response.LoginResponse;
import com.enigma.wmb_api.dto.response.RegisterResponse;

public interface AuthService {
    void initSuperAdmin();
    RegisterResponse register(RegisterRequest request);
    RegisterResponse registerAdmin(RegisterRequest request);
    LoginResponse login(LoginRequest request);

}
