package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.repository.CustomerRepository;
import com.enigma.wmb_api.service.CustomerService;
import com.enigma.wmb_api.service.UserService;
import com.enigma.wmb_api.specification.CustomerSpecification;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final ValidationUtil validationUtil;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer creat(Customer customer) {
        validationUtil.validate(customer);
        return repository.saveAndFlush(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponse getOneById(String id) {
        Optional<Customer> customer = repository.findById(id);
        if (customer.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);

        return convertCustomerToCustomerResponse(customer.get());
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CustomerResponse> getAll(SearchCustomerRequest request) {
        Sort sorting = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize(), sorting);

        Specification<Customer> specification = CustomerSpecification.getSpecification(request);
        Page<Customer> customers = repository.findAll(specification, pageable);

        return customers.map(this::convertCustomerToCustomerResponse);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse update(CustomerRequest request) {
        validationUtil.validate(request);
        Customer customerSelected = getById(request.getId());

        customerSelected.setName(request.getName());
        customerSelected.setIsMember(request.getIsMember());
        customerSelected.setMobilePhoneNumber(request.getMobilePhoneNumber());

        Customer customerResponse = repository.saveAndFlush(customerSelected);

        return convertCustomerToCustomerResponse(customerResponse);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Customer customer = getById(id);
        repository.deleteById(id);
        userService.updateIsEnable(customer.getUserAccount().getId(), false);
    }

    private CustomerResponse convertCustomerToCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .mobilePhoneNumber(customer.getMobilePhoneNumber())
                .isMember(customer.getIsMember())
                .userAccountId(customer.getUserAccount().getId())
                .build();
    }

}
