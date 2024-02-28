package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.TABLE_API)
public class TableController {

    private final TableService service;

    @PostMapping
    public ResponseEntity<CommonResponse<DiningTable>> creatTable(@RequestBody DiningTable diningTable) {

        DiningTable tableResult = service.creat(diningTable);

        CommonResponse<DiningTable> response = CommonResponse.<DiningTable>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Success create new table")
                .data(tableResult)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
