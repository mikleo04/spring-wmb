package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.SearchUSerAccountResquest;
import com.enigma.wmb_api.dto.request.UpdateUserAccountRequest;
import com.enigma.wmb_api.dto.response.UserAccountResponse;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.repository.UserAccountRepository;
import com.enigma.wmb_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    @Override
    public UserAccount getByuserId(String id) {
        return userAccountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    @Override
    public UserAccountResponse getOneById(String id) {
        UserAccount userAccount = userAccountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not foun"));
        return UserAccountResponse.builder()
                .id(userAccount.getId())
                .email(userAccount.getEmail())
                .password(userAccount.getPassword())
                .isEnable(userAccount.getIsEnable())
                .roles(userAccount.getRole().stream().map(Role::getRole).toList())
                .build();
    }

    @Override
    public Page<UserAccountResponse> getAll(SearchUSerAccountResquest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sorting = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize(), sorting);
        Page<UserAccount> userAccounts = userAccountRepository.findAll(pageable);

        return userAccounts.map(account -> {
            return UserAccountResponse.builder()
                    .id(account.getId())
                    .email(account.getEmail())
                    .password(account.getPassword())
                    .isEnable(account.getIsEnable())
                    .roles(account.getRole().stream().map(Role::getRole).toList())
                    .build();
        });
    }

    @Override
    public UserAccount getByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userAccountRepository.findByEmail(authentication.getPrincipal().toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public void updateEmailOrPassword(UpdateUserAccountRequest request) {

        String userAccountId = request.getUserAccountId();
        UserAccount userAccountLogin = getByContext();
        List<String> roles = userAccountLogin.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        if (userAccountLogin.getId().equals(userAccountId) || roles.contains(UserRole.ROLE_ADMIN.name()) || roles.contains(UserRole.ROLE_SUPER_ADMIN.name())) {
            UserAccount userAccount = getByuserId(request.getUserAccountId());
            if (request.getEmail() != null) {
                userAccount.setEmail(request.getEmail());
            }
            if (request.getPassword() != null) {
                userAccount.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            userAccountRepository.saveAndFlush(userAccount);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access not allowed");
        }

    }

    @Override
    public void updateIsEnable(String id, Boolean isEnable) {
        getByuserId(id);
        log.info("Masuk ni id nya {}", id);
        userAccountRepository.updateIsEnable(id, isEnable);
        log.info("Masuk ni id nya {}", id);
    }

}
