package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.SearchTableRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    @GetMapping("{id}")
    public ResponseEntity<CommonResponse<DiningTable>> getTableByid(@PathVariable String id) {
        DiningTable tableResult = service.getById(id);
        CommonResponse<DiningTable> response = CommonResponse.<DiningTable>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get table")
                .data(tableResult)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<DiningTable>>> getAllTable(
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
        Page<DiningTable> tablesResult = service.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(tablesResult.getTotalPages())
                .totalElement(tablesResult.getTotalElements())
                .page(tablesResult.getPageable().getPageNumber() + 1)
                .size(tablesResult.getSize())
                .hasNext(tablesResult.hasNext())
                .hasPrevious(tablesResult.hasPrevious())
                .build();

        CommonResponse<List<DiningTable>> response = CommonResponse.<List<DiningTable>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Succes get tables")
                .data(tablesResult.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);

    }

}
