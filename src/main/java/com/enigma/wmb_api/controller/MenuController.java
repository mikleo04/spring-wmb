package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.MenuResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.service.MenuService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Menu", description = "The Menu API. Contains all the operations that can be performed on a menu.")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.MENU_API)
public class MenuController {

    private final MenuService service;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Create menu")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces =MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MenuResponse>> createMenu(
            @RequestPart(name = "menu") String jsonMenu,
            @RequestPart(name = "images" ,required = false) List<MultipartFile> images
    ) {
        CommonResponse.CommonResponseBuilder<MenuResponse> responseBuilder = CommonResponse.builder();

        try {
            MenuRequest request = objectMapper.readValue(jsonMenu, new TypeReference<>() {});
            request.setImages(images);

            MenuResponse menuResponse = service.create(request);

            responseBuilder.statusCode(HttpStatus.CREATED.value());
            responseBuilder.message("Success creat menu");
            responseBuilder.data(menuResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
            responseBuilder.message("Internal server error woi");
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBuilder.build());
        }

    }

    @Operation(summary = "Get menu by id")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'CUSTOMER')")
    @GetMapping("{id}")
    public ResponseEntity<CommonResponse<MenuResponse>> getMenuById(@PathVariable String id) {
        MenuResponse menuResult = service.getOneById(id);

        CommonResponse<MenuResponse> response = CommonResponse.<MenuResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get menu")
                .data(menuResult)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get all menu")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'CUSTOMER')")
    @GetMapping
    public ResponseEntity<CommonResponse<List<MenuResponse>>> getAllMenu(
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
        Page<MenuResponse> menusResult = service.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(menusResult.getTotalPages())
                .totalElement(menusResult.getTotalElements())
                .page(menusResult.getPageable().getPageNumber() + 1)
                .size(menusResult.getSize())
                .hasNext(menusResult.hasNext())
                .hasPrevious(menusResult.hasPrevious())
                .build();

        CommonResponse<List<MenuResponse>> response = CommonResponse.<List<MenuResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get menus")
                .data(menusResult.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update menu")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MenuResponse>> updateMenu(
            @RequestPart(name = "menu") String jsonMenu,
            @RequestPart(name = "images", required = false) List<MultipartFile> images
    ) {
        CommonResponse.CommonResponseBuilder<MenuResponse> responseBuilder = CommonResponse.builder();

        try {
            MenuRequest request = objectMapper.readValue(jsonMenu, new TypeReference<>() {});
            request.setImages(images);

            MenuResponse menuResponse = service.update(request);

            responseBuilder.statusCode(HttpStatus.OK.value());
            responseBuilder.message("Success update menu");
            responseBuilder.data(menuResponse);
            return ResponseEntity.status(HttpStatus.OK).body(responseBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
            responseBuilder.message("Internal server error woi");
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBuilder.build());
        }
    }

    @Operation(summary = "Delete menu by id")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse<String>> deleteMenu(@PathVariable String id) {
        service.delete(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message("Success delete menu")
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Operation(description = "Update status menu by id")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
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
