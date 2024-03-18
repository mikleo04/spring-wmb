package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer", description = "The Customer API. Contains all the operations that can be performed on a customer.")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.CUSTOMER_API)
public class CustomerController {

    private final CustomerService service;

    @Operation(summary = "Get customer by id")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        CustomerResponse customerResult = service.getOneById(id);

        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(customerResult)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all customer")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomer(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "isMember", required = false) Boolean isMember
    ) {
        SearchCustomerRequest request = SearchCustomerRequest.builder()
                .sortBy(sortBy)
                .direction(direction)
                .page(page)
                .size(size)
                .name(name)
                .isMember(isMember)
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
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(customersResult.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update customer")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN') or @accountValidation.validationAccountCustomer(#request)")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(@RequestBody CustomerRequest request) {
        CustomerResponse customerResult = service.update(request);

        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(customerResult)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Operation(summary = "Delete customer by id")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<CommonResponse<String>> deleteCustomer(@PathVariable String id) {
        service.delete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

}
