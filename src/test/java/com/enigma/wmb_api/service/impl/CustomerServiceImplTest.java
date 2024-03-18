package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.repository.CustomerRepository;
import com.enigma.wmb_api.service.CustomerService;
import com.enigma.wmb_api.service.UserService;
import com.enigma.wmb_api.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private UserService userService;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(
                customerRepository,
                validationUtil,
                userService
        );
    }

    @Test
    void shouldReturnCustomerWhenCreate() {
        //Given
        Customer parameterCustomer = Customer.builder()
                .id("Customer-01")
                .name("Customer")
                .isMember(true)
                .mobilePhoneNumber("083212876512")
                .userAccount(UserAccount.builder().build())
                .build();

        validationUtil.validate(parameterCustomer);
        // stubbing
        Mockito.when(customerRepository.saveAndFlush(parameterCustomer))
                .thenReturn(parameterCustomer);
        //When
        Customer actualCustomer = customerService.creat(parameterCustomer);

        //Then
        assertEquals(parameterCustomer.getName(), actualCustomer.getName());
        assertEquals(parameterCustomer.getId(), actualCustomer.getId());
    }

    @Test
    void shouldReturnCustomerResponseWhenGetOneById() {
        //Given
        String id = "Customer-01";
        Customer returnCustomer = Customer.builder()
                .id("Customer-01")
                .name("Customer")
                .isMember(true)
                .mobilePhoneNumber("083212876512")
                .userAccount(UserAccount.builder().build())
                .build();

        //stubbing
        Mockito.when(customerRepository.findById(id))
                .thenReturn(Optional.ofNullable(returnCustomer));

        //When
        CustomerResponse actualCustomer = customerService.getOneById(id);

        //Then
        assertEquals(id, actualCustomer.getId());
        Mockito.verify(customerRepository, Mockito.times(1))
                .findById(id);
    }

    @Test
    void shoulThrowResponseStatusExceptionWhenNotFound() {

        String id = "Customer-01";

        //stubbing
        Mockito.when(customerRepository.findById(id))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));

        //When //Then
        assertThrows(ResponseStatusException.class, () -> {
            customerService.getById(id);
        });
        assertThrows(ResponseStatusException.class, () -> {
            customerService.getOneById(id);
        });

    }

    @Test
    void shouldReturnCustomerWhenGetOneById() {
        //Given
        String id = "Customer-01";
        Customer returnCustomer = Customer.builder()
                .id("Customer-01")
                .name("Customer")
                .isMember(true)
                .mobilePhoneNumber("083212876512")
                .userAccount(UserAccount.builder().build())
                .build();

        //stubbing
        Mockito.when(customerRepository.findById(id))
                .thenReturn(Optional.ofNullable(returnCustomer));

        //When
        Customer actualCustomer = customerService.getById(id);

        //Then
        assertEquals(id, actualCustomer.getId());
        Mockito.verify(customerRepository, Mockito.times(1))
                .findById(id);
    }

    @Test
    void shouldReturnPageCustomerResponseWhenGetAll() {
        //Given
        List<Customer> customers = List.of(
                Customer.builder()
                        .id("Customer-01")
                        .name("Customer")
                        .isMember(true)
                        .mobilePhoneNumber("083212876512")
                        .userAccount(UserAccount.builder().build())
                        .build(),
                Customer.builder()
                        .id("Customer-02")
                        .name("Customer2")
                        .isMember(true)
                        .mobilePhoneNumber("083212876522")
                        .userAccount(UserAccount.builder().build())
                        .build()
        );

        Page<Customer> returnCustomer = new PageImpl<>(customers);

        SearchCustomerRequest customerRequest = SearchCustomerRequest.builder()
                .page(1)
                .size(10)
                .direction("ASC")
                .sortBy("name")
                .build();

        //Stubbing
        Mockito.when(customerRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(returnCustomer);

        //When
        Page<CustomerResponse> actualCustomerResponses = customerService.getAll(customerRequest);

        //Then
        assertEquals(returnCustomer.getSize(), actualCustomerResponses.getSize());
    }

    @Test
    void shoulReturnCustomerResponseWhenUpdate() {
        //Given
        CustomerRequest customerRequest = CustomerRequest.builder()
                .id("Customer-01")
                .name("Customer")
                .isMember(true)
                .mobilePhoneNumber("081234762389")
                .build();

        UserAccount userAccount = UserAccount.builder()
                .email("customer@gmail.com")
                .password("12345")
                .role(List.of(Role.builder().role(UserRole.ROLE_SUPER_ADMIN).build()))
                .isEnable(true)
                .build();

        Customer currentCustomer = Customer.builder()
                .id("Customer-01")
                .name("Customer")
                .isMember(true)
                .mobilePhoneNumber("083212876512")
                .userAccount(userAccount)
                .build();

        validationUtil.validate(customerRequest);
        //stubbing config
        Mockito.when(customerRepository.findById(customerRequest.getId()))
                .thenReturn(Optional.ofNullable(currentCustomer));

        assert currentCustomer != null;
        currentCustomer.setName(customerRequest.getName());
        currentCustomer.setIsMember(customerRequest.getIsMember());
        currentCustomer.setMobilePhoneNumber(customerRequest.getMobilePhoneNumber());

        Mockito.when(customerRepository.saveAndFlush(currentCustomer))
                .thenReturn(currentCustomer);

        //When
        CustomerResponse actualCustomerResponse = customerService.update(customerRequest);

        //Then
        assertEquals(customerRequest.getName(), actualCustomerResponse.getName());
    }

    @Test
    void shouldCallDeleteOnce() {
        //Given
        String id = "Customer-01";
        UserAccount userAccount = UserAccount.builder()
                .id("userAccount-1")
                .email("customer@gmail.com")
                .password("12345")
                .role(List.of(Role.builder().role(UserRole.ROLE_SUPER_ADMIN).build()))
                .isEnable(true)
                .build();

        Customer returnCustomer = Customer.builder()
                .id("Customer-01")
                .name("Customer")
                .isMember(true)
                .mobilePhoneNumber("083212876512")
                .userAccount(userAccount)
                .build();

        Mockito.when(customerRepository.findById(id))
                .thenReturn(Optional.ofNullable(returnCustomer));
        assert returnCustomer != null;
        Mockito.doNothing().when(customerRepository).deleteById(id);
        Mockito.doNothing().when(userService).updateIsEnable(userAccount.getId(), false);

        // When
        customerService.delete(id);

        //Then
        Mockito.verify(customerRepository, Mockito.times(1)).deleteById(id);
        Mockito.verify(userService, Mockito.times(1)).updateIsEnable(userAccount.getId(), false);

    }
}