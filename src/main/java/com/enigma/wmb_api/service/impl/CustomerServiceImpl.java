package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.repository.CustomerRepository;
import com.enigma.wmb_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private  final CustomerRepository repository;

    @Override
    public CustomerResponse creat(CustomerRequest request) {

        Customer customer = Customer.builder()
                .name(request.getName())
                .mobilePhoneNumber(request.getMobilePhoneNumber())
                .isMember(request.getIsMember())
                .build();

        Customer customerResponse = repository.saveAndFlush(customer);

        return CustomerResponse.builder()
                .id(customerResponse.getId())
                .name(customerResponse.getName())
                .mobilePhoneNumber(customerResponse.getMobilePhoneNumber())
                .isMember(customerResponse.getIsMember())
                .build();
    }

    @Override
    public CustomerResponse getById(String id) {
        Optional<Customer> customer = repository.findById(id);
        if (customer.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");

        return CustomerResponse.builder()
                .id(customer.get().getId())
                .name(customer.get().getName())
                .mobilePhoneNumber(customer.get().getMobilePhoneNumber())
                .isMember(customer.get().getIsMember())
                .build();
    }
}
