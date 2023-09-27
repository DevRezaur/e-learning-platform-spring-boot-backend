package com.devrezaur.common.module.util;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.model.CustomHttpResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.REQUEST_ID;

@Component
public class HttpCallLogic {

    private final RestTemplate restTemplate;
    private final Logger systemLogger;

    public HttpCallLogic(@Qualifier("systemLogger") Logger systemLogger, RestTemplate restTemplate) {
        this.systemLogger = systemLogger;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<CustomHttpResponse> executeRequest(CustomHttpRequest customHttpRequest) {
        URI url = prepareRequestUri(customHttpRequest);
        HttpMethod methodType = customHttpRequest.getMethodType();
        HttpHeaders requestHeaders = prepareRequestHeaders(customHttpRequest);
        MultiValueMap<String, Object> requestBody = prepareRequestBody(customHttpRequest);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        return restTemplate.exchange(url, methodType, requestEntity, CustomHttpResponse.class);
    }

    private URI prepareRequestUri(CustomHttpRequest customHttpRequest) {
        String url = customHttpRequest.getUrl();
        Map<String, String> urlParameterMap = customHttpRequest.getUrlParameterMap();
        url = insertPathVariablesInUrl(url, urlParameterMap);
        insertPathVariablesInUrl(url, urlParameterMap);
        return appendQueryParametersToUrl(url, urlParameterMap);
    }

    private String insertPathVariablesInUrl(String url, Map<String, String> urlParameterMap) {
        for (Map.Entry<String, String> element : urlParameterMap.entrySet()) {
            String key = element.getKey();
            String value = element.getValue();
            String pathVariableKey = String.format("{%s}", key);
            if (url.contains(pathVariableKey)) {
                url = url.replace(pathVariableKey, value);
                urlParameterMap.remove(key);
            }
        }
        return url;
    }

    private URI appendQueryParametersToUrl(String url, Map<String, String> urlParameterMap) {
        StringBuilder queryParameters = new StringBuilder();
        queryParameters.append(url.contains("?") ? "&" : "?");
        for (Map.Entry<String, String> element : urlParameterMap.entrySet()) {
            String key = element.getKey();
            String value = element.getValue();
            queryParameters.append(key);
            queryParameters.append("=");
            queryParameters.append(value);
            queryParameters.append("&");
        }
        try {
            return new URI(url + queryParameters.substring(0, queryParameters.length() - 1));
        } catch (URISyntaxException ex) {
            systemLogger.error("HttpCallLogic: Exception occurred while building URI!");
        }
        return null;
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
