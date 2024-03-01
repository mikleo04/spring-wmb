package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.response.CustomerResponse;

public interface CustomerService {
    CustomerResponse creat(CustomerRequest request);
    CustomerResponse getById(String id);
}
