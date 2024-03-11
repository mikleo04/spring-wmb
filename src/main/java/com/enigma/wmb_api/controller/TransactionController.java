package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.TransactionStatus;
import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.SearchTransactionRequest;
import com.enigma.wmb_api.dto.request.TransactionRequest;
import com.enigma.wmb_api.dto.request.UpdateStatusTransactionRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.dto.response.TransactionResponse;
import com.enigma.wmb_api.service.TransactionService;
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
import java.util.Map;

@Tag(name = "Transaction", description = "The Transaction API. Contains all the operations that can be performed on a transaction.")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.TRANSACTION_API)
public class TransactionController {

    private final TransactionService service;

    @Operation(summary = "Create transaction")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'CUSTOMER')")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TransactionResponse>> createTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse transactionResult = service.create(request);

        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Success create transaction")
                .data(transactionResult)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all transaction")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getAllTransaction(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "transDate") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction
    ) {
        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();

        Page<TransactionResponse> transactionsResult = service.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(transactionsResult.getTotalPages())
                .totalElement(transactionsResult.getTotalElements())
                .page(transactionsResult.getPageable().getPageNumber() + 1)
                .size(transactionsResult.getSize())
                .hasNext(transactionsResult.hasNext())
                .hasPrevious(transactionsResult.hasPrevious())
                .build();

        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get all transactions")
                .data(transactionsResult.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transaction by id")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TransactionResponse>> getTransactionById(@PathVariable String id) {
        TransactionResponse transactionResult = service.getById(id);

        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get transaction")
                .data(transactionResult)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update status transaction after payment")
    @PostMapping("/status")
    public ResponseEntity<CommonResponse<?>> updateStatusTransaction(
            @RequestBody Map<String, Object> request
    ) {
        UpdateStatusTransactionRequest statusTransactionRequest = UpdateStatusTransactionRequest.builder()
                .orderId(request.get("order_id").toString())
                .transactionStatus(TransactionStatus.valueOf(request.get("transaction_status").toString().toUpperCase()))
                .build();

        service.updateStatus(statusTransactionRequest);

        return ResponseEntity.ok(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success update status transaction")
                .build());

    }

}
