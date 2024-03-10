package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.UpdateUserAccountRequest;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.repository.UserAccountRepository;
import com.enigma.wmb_api.service.UserService;
import lombok.RequiredArgsConstructor;
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

}
