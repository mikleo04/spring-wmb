package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.TransactionType;
import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.TransTypeResponse;
import com.enigma.wmb_api.service.TransTypeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.TRANS_TYPE_API)
public class TransTypeController {
    private final TransTypeService service;

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
