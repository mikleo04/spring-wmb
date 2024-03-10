package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.UpdateUserAccountRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.USER_ACCOUT_API)
public class UserAccountController {

    private final UserService userService;

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

}
