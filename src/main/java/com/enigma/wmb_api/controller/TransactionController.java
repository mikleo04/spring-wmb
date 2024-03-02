package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.SearchTransactionRequest;
import com.enigma.wmb_api.dto.request.TransactionRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.dto.response.TransactionResponse;
import com.enigma.wmb_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.TRANSACTION_API)
public class TransactionController {

    private final TransactionService service;

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

}