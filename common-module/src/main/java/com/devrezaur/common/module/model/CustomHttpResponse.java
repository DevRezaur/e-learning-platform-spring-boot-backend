package com.devrezaur.common.module.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
public class CustomHttpResponse {

    private UUID requestId;
    private HttpStatus httpStatusCode;
    private Map<String, Object> responseBody;
    private String errorCode;
    private String errorMessage;
}
