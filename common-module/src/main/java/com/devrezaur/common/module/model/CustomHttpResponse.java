package com.devrezaur.common.module.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String errorCode;
    private String errorMessage;
}
