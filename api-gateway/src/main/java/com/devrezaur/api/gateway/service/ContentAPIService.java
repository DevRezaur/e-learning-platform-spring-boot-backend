package com.devrezaur.api.gateway.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.common.module.util.RequestBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContentAPIService {

    private final HttpCallLogic httpCallLogic;

    public ContentAPIService(HttpCallLogic httpCallLogic) {
        this.httpCallLogic = httpCallLogic;
    }

    public ResponseEntity<byte[]> serveContent(String contentUrl) throws Exception {
        String url = "content-delivery-service/file-system-storage/" + contentUrl;
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put("Accept", MediaType.APPLICATION_OCTET_STREAM_VALUE);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.GET, url, headerParameterMap, null, null);
        try {
            return httpCallLogic.fetchMediaContent(customHttpRequest);
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling CONTENT-DELIVERY-SERVICE!");
        }
    }
}
