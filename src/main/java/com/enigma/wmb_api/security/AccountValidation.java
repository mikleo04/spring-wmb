package com.enigma.wmb_api.security;

import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.UpdateUserAccountRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.service.CustomerService;
import com.enigma.wmb_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidation {

    private final CustomerService customerService;
    private final UserService userService;

    public Boolean validationAccountCustomer(CustomerRequest request) {
        Customer customer = customerService.getById(request.getId());
        UserAccount userAccount = userService.getByContext();

        return customer.getUserAccount().getId().equals(userAccount.getId());
    }

    public Boolean validationAccound(UpdateUserAccountRequest request) {
        UserAccount userAccount = userService.getByContext();

        return request.getUserAccountId().equals(userAccount.getId());
    }

}
