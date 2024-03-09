package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.CUSTOMER_API)
public class CustomerController {
    private final CustomerService service;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        CustomerResponse customerResult = service.getOneById(id);

        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get customer")
                .data(customerResult)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomer(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction
    ) {
        SearchCustomerRequest request = SearchCustomerRequest.builder()
                .sortBy(sortBy)
                .direction(direction)
                .page(page)
                .size(size)
                .build();

        Page<CustomerResponse> customersResult = service.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(customersResult.getTotalPages())
                .totalElement(customersResult.getTotalElements())
                .page(customersResult.getPageable().getPageNumber() + 1)
                .size(customersResult.getSize())
                .hasNext(customersResult.hasNext())
                .hasPrevious(customersResult.hasPrevious())
                .build();

        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get all customer")
                .data(customersResult.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

//    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(@RequestBody CustomerRequest request) {
        CustomerResponse customerResult = service.update(request);

        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message("Success update customer")
                .data(customerResult)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<CommonResponse<String>> deleteCustomer(@PathVariable String id) {
        service.delete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message("Success delete customer")
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

}
