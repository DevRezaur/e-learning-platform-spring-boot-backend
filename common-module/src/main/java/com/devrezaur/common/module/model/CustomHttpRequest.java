package com.devrezaur.common.module.model;

import lombok.*;
import org.springframework.http.HttpMethod;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomHttpRequest {
    private String requestId;
    private HttpMethod methodType;
    private String url;
    private Map<String, String> headerParameterMap;
    private Map<String, String> queryParameterMap;
    private Map<String, Object> bodyParameterMap;
}
