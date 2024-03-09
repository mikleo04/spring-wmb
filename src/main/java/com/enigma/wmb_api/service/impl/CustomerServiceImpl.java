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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final ValidationUtil validationUtil;

    @Override
    public Customer creat(Customer customer) {
        validationUtil.validate(customer);
        return repository.saveAndFlush(customer);
    }

    @Override
    public CustomerResponse getOneById(String id) {
        Optional<Customer> customer = repository.findById(id);
        if (customer.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");

        return CustomerResponse.builder()
                .id(customer.get().getId())
                .name(customer.get().getName())
                .mobilePhoneNumber(customer.get().getMobilePhoneNumber())
                .isMember(customer.get().getIsMember())
                .userAccountId(customer.get().getUserAccount().getId())
                .build();
    }

    @Override
    public Customer getById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
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
                    .userAccountId(customer.getUserAccount().getId())
                    .build();
        });

    }

    @Override
    public CustomerResponse update(CustomerRequest request) {
        Customer customerSelected = getById(request.getId());

        customerSelected.setName(request.getName());
        customerSelected.setIsMember(request.getIsMember());
        customerSelected.setMobilePhoneNumber(request.getMobilePhoneNumber());

        Customer customerResponse = repository.saveAndFlush(customerSelected);

        return CustomerResponse.builder()
                .id(customerResponse.getId())
                .name(customerResponse.getName())
                .mobilePhoneNumber(customerResponse.getMobilePhoneNumber())
                .isMember(customerResponse.getIsMember())
                .userAccountId(customerResponse.getUserAccount().getId())
                .build();
    }

    @Override
    public void delete(String id) {
        getById(id);
        repository.deleteById(id);
    }
}
