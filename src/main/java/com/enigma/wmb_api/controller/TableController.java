package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.SearchTableRequest;
import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.dto.response.TableResponse;
import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.TABLE_API)
public class TableController {

    private final TableService service;

    @PostMapping
    public ResponseEntity<CommonResponse<TableResponse>> creatTable(@RequestBody TableRequest request) {

        TableResponse tableResult = service.creat(request);

        CommonResponse<TableResponse> response = CommonResponse.<TableResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Success create new table")
                .data(tableResult)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<CommonResponse<TableResponse>> getTableByid(@PathVariable String id) {
        TableResponse tableResult = service.getById(id);
        CommonResponse<TableResponse> response = CommonResponse.<TableResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get table")
                .data(tableResult)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TableResponse>>> getAllTable(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction
    ) {
        SearchTableRequest request = SearchTableRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();
        Page<TableResponse> tablesResult = service.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(tablesResult.getTotalPages())
                .totalElement(tablesResult.getTotalElements())
                .page(tablesResult.getPageable().getPageNumber() + 1)
                .size(tablesResult.getSize())
                .hasNext(tablesResult.hasNext())
                .hasPrevious(tablesResult.hasPrevious())
                .build();

        CommonResponse<List<TableResponse>> response = CommonResponse.<List<TableResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get tables")
                .data(tablesResult.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse<String>> deleteTable(@PathVariable String id) {
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success delete table")
                .build();

        return ResponseEntity.ok(response);
    }

}
