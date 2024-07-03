package com.devrezaur.common.module.util;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.CONTENT_TYPE_HEADER_KEY;
import static com.devrezaur.common.module.constant.CommonConstant.REQUEST_ID;

@Component
public class HttpCallLogic {

    private final Logger systemLogger;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public HttpCallLogic(@Qualifier("systemLogger") Logger systemLogger, RestTemplate restTemplate,
                         ObjectMapper objectMapper) {
        this.systemLogger = systemLogger;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<CustomHttpResponse> executeRequest(CustomHttpRequest customHttpRequest) {
        URI url = prepareRequestUri(customHttpRequest);
        HttpMethod methodType = customHttpRequest.getMethodType();
        HttpHeaders requestHeaders = prepareRequestHeaders(customHttpRequest);
        Map<String, ?> requestBody = prepareRequestBody(customHttpRequest);
        HttpEntity<Map<String, ?>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        try {
            return restTemplate.exchange(url, methodType, requestEntity, CustomHttpResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            CustomHttpResponse customHttpResponse = buildErrorResponse(ex.getResponseBodyAsString());
            return new ResponseEntity<>(customHttpResponse, ex.getStatusCode());
        } catch (RestClientException ex) {

        }

        return null;
    }

    private CustomHttpResponse buildErrorResponse(String errorBody) {
        CustomHttpResponse customHttpResponse = null;
        try {
            customHttpResponse = objectMapper.readValue(errorBody, CustomHttpResponse.class);
        } catch (JsonProcessingException ex) {
            systemLogger.error("HttpCallLogic: Exception occurred building error response!");
        }
        return customHttpResponse;
    }

    public ResponseEntity<byte[]> fetchMediaContent(CustomHttpRequest customHttpRequest) {
        URI url = prepareRequestUri(customHttpRequest);
        HttpMethod methodType = customHttpRequest.getMethodType();
        HttpHeaders requestHeaders = prepareRequestHeaders(customHttpRequest);
        HttpEntity<Map<String, ?>> requestEntity = new HttpEntity<>(requestHeaders);
        return restTemplate.exchange(url, methodType, requestEntity, byte[].class);
    }

    private URI prepareRequestUri(CustomHttpRequest customHttpRequest) {
        String url = customHttpRequest.getUrl();
        Map<String, String> urlParameterMap = customHttpRequest.getUrlParameterMap();
        String queryParameters = "";
        if (urlParameterMap != null && !urlParameterMap.isEmpty()) {
            url = insertPathVariablesInUrl(url, urlParameterMap);
            insertPathVariablesInUrl(url, urlParameterMap);
            queryParameters = buildQueryParameters(url, urlParameterMap);
        }
        try {
            return new URI(url + queryParameters);
        } catch (URISyntaxException ex) {
            systemLogger.error("HttpCallLogic: Exception occurred while building URI!");
        }
        return null;
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

    private String buildQueryParameters(String url, Map<String, String> urlParameterMap) {
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
        return queryParameters.substring(0, queryParameters.length() - 1);
    }

    private HttpHeaders prepareRequestHeaders(CustomHttpRequest customHttpRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(REQUEST_ID, customHttpRequest.getRequestId());
        Map<String, String> headerParameterMap = customHttpRequest.getHeaderParameterMap();
        if (headerParameterMap != null && !headerParameterMap.isEmpty()) {
            for (Map.Entry<String, String> header : headerParameterMap.entrySet()) {
                httpHeaders.add(header.getKey(), header.getValue());
            }
        }
        return httpHeaders;
    }

    private Map<String, ?> prepareRequestBody(CustomHttpRequest customHttpRequest) {
        Map<String, Object> bodyMap = customHttpRequest.getBodyMap();
        boolean isMultipartFormDataHeaderPresent = isMultipartFormDataHeaderPresent(customHttpRequest);
        if (isMultipartFormDataHeaderPresent && bodyMap != null && !bodyMap.isEmpty()) {
            return prepareMultiValueRequestBody(bodyMap);
        }
        return bodyMap;
    }

    private boolean isMultipartFormDataHeaderPresent(CustomHttpRequest customHttpRequest) {
        Map<String, String> headerParameterMap = customHttpRequest.getHeaderParameterMap();
        if (headerParameterMap != null && headerParameterMap.containsKey(CONTENT_TYPE_HEADER_KEY)) {
            return headerParameterMap.get(CONTENT_TYPE_HEADER_KEY).equals(MediaType.MULTIPART_FORM_DATA_VALUE);
        }
        return false;
    }

    private MultiValueMap<String, Object> prepareMultiValueRequestBody(Map<String, Object> bodyMap) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> element : bodyMap.entrySet()) {
            if (element.getValue() instanceof MultipartFile[] multipartFiles) {
                for (MultipartFile multipartFile : multipartFiles) {
                    requestBody.add(element.getKey(), multipartFile.getResource());
                }
            } else {
                requestBody.add(element.getKey(), element.getValue());
            }
        }
        return requestBody;
    }
}
