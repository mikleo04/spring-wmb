package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchUSerAccountResquest;
import com.enigma.wmb_api.dto.request.UpdateUserAccountRequest;
import com.enigma.wmb_api.dto.response.UserAccountResponse;
import com.enigma.wmb_api.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserAccount getByuserId(String id);
    UserAccountResponse getOneById(String id);
    Page<UserAccountResponse> getAll(SearchUSerAccountResquest request);
    UserAccount getByContext();
    void updateEmailOrPassword(UpdateUserAccountRequest request);
    void updateIsEnable(String id, Boolean isEnable);
}
