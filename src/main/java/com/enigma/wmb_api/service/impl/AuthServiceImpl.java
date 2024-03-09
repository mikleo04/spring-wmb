package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.LoginRequest;
import com.enigma.wmb_api.dto.request.RegisterRequest;
import com.enigma.wmb_api.dto.response.LoginResponse;
import com.enigma.wmb_api.dto.response.RegisterResponse;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.repository.UserAccountRepository;
import com.enigma.wmb_api.service.AuthService;
import com.enigma.wmb_api.service.CustomerService;
import com.enigma.wmb_api.service.JwtService;
import com.enigma.wmb_api.service.RoleService;
import com.enigma.wmb_api.util.ValidationUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository repository;
    private final RoleService roleService;
    private final ValidationUtil validationUtil;
    private final CustomerService customerServicece;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${wmb.email.superadmin}")
    private String emailSuperAdmin;
    @Value("${wmb.password.superadmin}")
    private String passwordSuperAdmin;

    @PostConstruct
    @Override
    public void initSuperAdmin() {
        Optional<UserAccount> currentSuperAdmin = repository.findByEmail(emailSuperAdmin);

        if (currentSuperAdmin.isPresent()) return;

        List<Role> roles = List.of(
                roleService.getOrSave(UserRole.ROLE_SUPER_ADMIN),
                roleService.getOrSave(UserRole.ROLE_ADMIN),
                roleService.getOrSave(UserRole.ROLE_CUSTOMER)
        );

        UserAccount superAdmin = UserAccount.builder()
                .email(emailSuperAdmin)
                .isEnable(true)
                .password(passwordEncoder.encode(passwordSuperAdmin))
                .role(roles)
                .build();
        repository.saveAndFlush(superAdmin);
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        validationUtil.validate(request);
        Role role = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        UserAccount userAccount = UserAccount.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(List.of(role))
                .isEnable(true)
                .build();
        repository.saveAndFlush(userAccount);

        Customer customer = Customer.builder()
                .name(request.getName())
                .userAccount(userAccount)
                .build();
        customerServicece.creat(customer);

        List<String> roles = userAccount.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return RegisterResponse.builder()
                .name(customer.getName())
                .email(userAccount.getEmail())
                .role(roles.stream().map(UserRole::valueOf).toList())
                .build();

    }

    @Override
    public RegisterResponse registerAdmin(RegisterRequest request) {
        validationUtil.validate(request);
        List<Role> listRole = List.of(
                roleService.getOrSave(UserRole.ROLE_ADMIN),
                roleService.getOrSave(UserRole.ROLE_CUSTOMER)
        );

        UserAccount userAccount = UserAccount.builder()
                .email(request.getEmail())
                .role(listRole)
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnable(true)
                .build();
        repository.saveAndFlush(userAccount);

        Customer customer = Customer.builder()
                .name(request.getName())
                .userAccount(userAccount)
                .build();
        customerServicece.creat(customer);

        List<String> roles = userAccount.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return RegisterResponse.builder()
                .name(customer.getName())
                .email(userAccount.getEmail())
                .role(roles.stream().map(UserRole::valueOf).toList())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        UserAccount userAccount = (UserAccount) authenticate.getPrincipal();

        String token = jwtService.generateToken(userAccount);
        return LoginResponse.builder()
                .email(userAccount.getUsername())
                .role(userAccount.getAuthorities().stream().map(role -> UserRole.valueOf(role.getAuthority())).toList())
                .token(token)
                .build();
    }
}
