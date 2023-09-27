package com.devrezaur.common.module.model;

import lombok.*;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
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
    private Map<String, String> headerParameterMap = new HashMap<>();
    private Map<String, String> urlParameterMap = new HashMap<>();
    private Map<String, Object> bodyMap;
}
