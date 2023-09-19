package com.devrezaur.common.module.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Builder
@Getter
public class CustomHttpResponse {
    private HttpStatus httpStatus;
    private Map<String, Object> responseBody;
    private String customErrorCode;
    private String errorMessage;
}
