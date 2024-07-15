package com.devrezaur.common.module.exception;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<CustomHttpResponse> handleApiException(ApiException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        String errorCode = ex.getErrorCode();
        String errorMessage = ex.getErrorMessage();
        return ResponseBuilder.buildFailureResponse(httpStatus, errorCode, errorMessage);
    }

    @ExceptionHandler({AccessDeniedException.class, InsufficientAuthenticationException.class})
    public ResponseEntity<CustomHttpResponse> handleAccessDeniedException(Exception ex) {
        String errorMessage = null;
        if (ex.getMessage() != null) {
            errorMessage = ex.getMessage();
        } else if (ex.getCause() != null) {
            errorMessage = ex.getCause().toString();
        }
        return ResponseBuilder.buildFailureResponse(HttpStatus.UNAUTHORIZED, "401", errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomHttpResponse> handleUnexpectedException(Exception ex) {
        String errorMessage = null;
        if (ex.getMessage() != null) {
            errorMessage = ex.getMessage();
        } else if (ex.getCause() != null) {
            errorMessage = ex.getCause().toString();
        }
        return ResponseBuilder.buildFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", errorMessage);
    }
}
