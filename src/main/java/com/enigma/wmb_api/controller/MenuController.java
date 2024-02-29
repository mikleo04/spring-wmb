package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.MENU_API)
public class MenuController {

    private final MenuService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<Menu>> createMenu(@RequestBody Menu menu) {

        Menu menuResult = service.create(menu);

        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Success creat menu")
                .data(menuResult)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<CommonResponse<Menu>> getMenuById(@PathVariable String id) {
        Menu menuResult = service.getById(id);

        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get menu")
                .data(menuResult)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<Menu>>> getAllMenu(
        @RequestParam(name = "page", defaultValue = "1") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(name = "direction", defaultValue = "ASC") String direction
    ) {
        SearchMenuRequest request = SearchMenuRequest.builder()
                .size(size)
                .page(page)
                .direction(direction)
                .sortBy(sortBy)
                .build();
        Page<Menu> menusResult = service.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(menusResult.getTotalPages())
                .totalElement(menusResult.getTotalElements())
                .page(menusResult.getPageable().getPageNumber() + 1)
                .size(menusResult.getSize())
                .hasNext(menusResult.hasNext())
                .hasPrevious(menusResult.hasPrevious())
                .build();

        CommonResponse<List<Menu>> response = CommonResponse.<List<Menu>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get menus")
                .data(menusResult.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<Menu>> updateMenu(@RequestBody Menu menu) {
        Menu menuResult = service.update(menu);

        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message("Success update menu")
                .data(menuResult)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse<String>> deleteMenu(@PathVariable String id) {
        service.delete(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message("Success delete menu")
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<CommonResponse<String>> updateStatusMenu(@PathVariable String id, @RequestParam(name = "status") Boolean status) {
        service.updateStatus(id, status);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message("Succes update status menu")
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

}
