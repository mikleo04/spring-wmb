package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.dto.response.CommonResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<CommonResponse<?>> responseStatusExceptionHandler(ResponseStatusException exception) {
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(exception.getStatusCode().value())
                .message(exception.getReason())
                .build();

        return ResponseEntity.status(exception.getStatusCode()).body(response);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<CommonResponse<?>> constraintViolationException(ConstraintViolationException exception) {
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<CommonResponse<?>> dataIntegrityViolationException(DataIntegrityViolationException exception) {
        CommonResponse.CommonResponseBuilder<Object> commonResponseBuilder = CommonResponse.builder();

        HttpStatus httpStatus;

        if (exception.getMessage().contains("foreign key constraint")) {
            commonResponseBuilder.statusCode(HttpStatus.BAD_REQUEST.value());
            commonResponseBuilder.message("Can't delete this data because have realtion");
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (exception.getMessage().contains("unique constraint") || exception.getMessage().contains("Duplicate entry")) {
            commonResponseBuilder.statusCode(HttpStatus.CONFLICT.value());
            commonResponseBuilder.message("Data already exist");
            httpStatus = HttpStatus.CONFLICT;
        } else {
            commonResponseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            commonResponseBuilder.message("Internal Server Error");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(httpStatus).body(commonResponseBuilder.build());

    }

}
