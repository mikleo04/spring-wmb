package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.SearchUSerAccountResquest;
import com.enigma.wmb_api.dto.request.UpdateUserAccountRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.dto.response.UserAccountResponse;
import com.enigma.wmb_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Account", description = "The User Account API. Contains all the operations that can be performed on an account.")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.USER_ACCOUT_API)
public class UserAccountController {

    private final UserService userService;

    @Operation(summary = "Update user account")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<String>> updateAccount(@RequestBody UpdateUserAccountRequest request) {
        userService.updateEmailOrPassword(request);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success update account")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user account by id")
    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<UserAccountResponse>> getAccountById(@PathVariable(name = "id") String id) {
        UserAccountResponse userAccount = userService.getOneById(id);
        CommonResponse<UserAccountResponse> response = CommonResponse.<UserAccountResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get account")
                .data(userAccount)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all user account")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<UserAccountResponse>>> getAllAccount(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "email") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction
    ) {
        SearchUSerAccountResquest accountResquest = SearchUSerAccountResquest.builder()
                .direction(direction)
                .size(size)
                .sortBy(sortBy)
                .page(page)
                .build();
        Page<UserAccountResponse> userAccountResult = userService.getAll(accountResquest);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(userAccountResult.getTotalPages())
                .totalElement(userAccountResult.getTotalElements())
                .page(userAccountResult.getPageable().getPageNumber() + 1)
                .size(userAccountResult.getSize())
                .hasNext(userAccountResult.hasNext())
                .hasPrevious(userAccountResult.hasPrevious())
                .build();

        CommonResponse<List<UserAccountResponse>> response = CommonResponse.<List<UserAccountResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Succes get all account")
                .data(userAccountResult.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

}
