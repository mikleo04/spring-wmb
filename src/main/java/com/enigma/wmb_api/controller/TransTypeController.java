package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.TransactionType;
import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.TransTypeResponse;
import com.enigma.wmb_api.service.TransTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Transaction Type", description = "The Transaction Type API. Contains get transacton type.")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.TRANS_TYPE_API)
public class TransTypeController {
    private final TransTypeService service;

    @Operation(summary = "Get transaction type by id")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<CommonResponse<TransTypeResponse>> getTransTypeById(@PathVariable TransactionType id) {
        TransTypeResponse transTypeResult = service.getById(id);

        CommonResponse<TransTypeResponse> response = CommonResponse.<TransTypeResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get transaction type")
                .data(transTypeResult)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all transaction type")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<CommonResponse<List<TransTypeResponse>>> getAllTranstype(){
        List<TransTypeResponse> transTypesResult = service.getAll();

        CommonResponse<List<TransTypeResponse>> response = CommonResponse.<List<TransTypeResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get all transaction type")
                .data(transTypesResult)
                .build();

        return ResponseEntity.ok(response);
    }
}
