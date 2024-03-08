package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.AuthRequest;
import com.enigma.wmb_api.dto.response.LoginResponse;
import com.enigma.wmb_api.dto.response.RegisterResponse;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.repository.UserAccountRepository;
import com.enigma.wmb_api.service.AuthService;
import com.enigma.wmb_api.service.RoleService;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository repository;
    private final RoleService roleService;
    private final ValidationUtil validationUtil;

    @Override
    public RegisterResponse register(AuthRequest request) {
        validationUtil.validate(request);
        Role role = roleService.getOrSave(UserRole.CUSTOMER);

        UserAccount userAccount = UserAccount.builder()
                .email(request.getEmail())
                .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                .role(List.of(role))
                .isEnable(true)
                .build();
        repository.saveAndFlush(userAccount);

        List<String > roles = userAccount.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return RegisterResponse.builder()
                .email(userAccount.getEmail())
                .role(roles.stream().map(UserRole::valueOf).toList())
                .build();

    }

    @Override
    public RegisterResponse registerAdmin(AuthRequest request) {
        return null;
    }

    @Override
    public LoginResponse login(AuthRequest request) {
        return null;
    }
}
