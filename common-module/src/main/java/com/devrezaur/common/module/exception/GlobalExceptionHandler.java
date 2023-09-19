package com.devrezaur.common.module.exception;

import com.devrezaur.common.module.model.CustomHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public CustomHttpResponse handleException(Exception ex) {
        return CustomHttpResponse
                .builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .customErrorCode("500")
                .errorMessage(String.valueOf(ex.getCause()))
                .build();
    }
}
