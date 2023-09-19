package com.devrezaur.user.service.controller;

import com.devrezaur.common.module.model.CustomHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public CustomHttpResponse handleException() {
        return CustomHttpResponse
                .builder()
                .requestId(requestId)
                .httpStatusCode(HttpStatus.EXPECTATION_FAILED)
                .errorCode("417")
                .errorMessage("Failed to update user information! Reason: " + ex.getCause())
                .build();
    }
}
