package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserAccount getByuserId(String id);
    UserAccount getByContext();
}
