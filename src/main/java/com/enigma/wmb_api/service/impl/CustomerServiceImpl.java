package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.repository.CustomerRepository;
import com.enigma.wmb_api.service.CustomerService;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final ValidationUtil validationUtil;

    @Override
    public CustomerResponse creat(CustomerRequest request) {
        validationUtil.validate(request);

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

    @Override
    public Page<CustomerResponse> getAll(SearchCustomerRequest request) {
        Sort sorting = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize(), sorting);

        Page<Customer> customers = repository.findAll(pageable);

        return customers.map(customer -> {
            return CustomerResponse.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .mobilePhoneNumber(customer.getMobilePhoneNumber())
                    .isMember(customer.getIsMember())
                    .build();
        });

    }

    @Override
    public CustomerResponse update(CustomerRequest request) {
        getById(request.getId());

        Customer customer = Customer.builder()
                .id(request.getId())
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
    public void delete(String id) {
        getById(id);
        repository.deleteById(id);
    }
}
