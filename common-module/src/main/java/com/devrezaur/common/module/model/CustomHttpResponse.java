package com.devrezaur.common.module.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomHttpResponse {
    private HttpStatus httpStatus;
    private Map<String, Object> responseBody;
    private String customErrorCode;
    private String errorMessage;
}
