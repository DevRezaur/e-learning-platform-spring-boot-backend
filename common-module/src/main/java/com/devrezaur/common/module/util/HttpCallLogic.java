package com.devrezaur.common.module.util;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.model.CustomHttpResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.REQUEST_ID;

@Component
public class HttpCallLogic {

    private final RestTemplate restTemplate;

    public HttpCallLogic(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<CustomHttpResponse> executeRequest(CustomHttpRequest customHttpRequest) {
        String url = customHttpRequest.getUrl();
        HttpMethod methodType = customHttpRequest.getMethodType();
        HttpHeaders requestHeaders = prepareRequestHeaders(customHttpRequest);
        MultiValueMap<String, Object> requestBody = prepareRequestBody(customHttpRequest);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        return restTemplate.exchange(url, methodType, requestEntity, CustomHttpResponse.class);
    }

    private HttpHeaders prepareRequestHeaders(CustomHttpRequest customHttpRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(REQUEST_ID, customHttpRequest.getRequestId());
        Map<String, String> headerParameterMap = customHttpRequest.getHeaderParameterMap();
        if (!headerParameterMap.isEmpty()) {
            for (Map.Entry<String, String> header : headerParameterMap.entrySet()) {
                httpHeaders.add(header.getKey(), header.getValue());
            }
        }
        return httpHeaders;
    }

    private MultiValueMap<String, Object> prepareRequestBody(CustomHttpRequest customHttpRequest) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        Map<String, Object> bodyMap = customHttpRequest.getBodyMap();
        if (!bodyMap.isEmpty()) {
            for (Map.Entry<String, Object> element : bodyMap.entrySet()) {
                requestBody.add(element.getKey(), element.getValue());
            }
        }
        return requestBody;
    }
}
