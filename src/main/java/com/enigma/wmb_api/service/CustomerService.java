package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Customer creat(Customer customer);
    CustomerResponse getOneById(String id);
    Customer getById(String id);
    Page<CustomerResponse> getAll(SearchCustomerRequest request);
    CustomerResponse update(CustomerRequest request);
    void delete(String id);
}
