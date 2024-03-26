package com.devrezaur.api.gateway.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.common.module.util.RequestBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.ACCEPT_HEADER_KEY;
import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;
import static com.devrezaur.common.module.constant.CommonConstant.CONTENT_TYPE_HEADER_KEY;

@Service
public class ContentAPIService {

    private final HttpCallLogic httpCallLogic;

    public ContentAPIService(HttpCallLogic httpCallLogic) {
        this.httpCallLogic = httpCallLogic;
    }

    public ResponseEntity<byte[]> serveContent(String contentUrl) throws Exception {
        String url = "content-delivery-service/file-system-storage/" + contentUrl;
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put(ACCEPT_HEADER_KEY, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.GET, url, headerParameterMap, null, null);
        try {
            return httpCallLogic.fetchMediaContent(customHttpRequest);
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling CONTENT-DELIVERY-SERVICE!");
        }
    }

    public List<String> saveContents(MultipartFile[] contents, String accessToken) throws Exception {
        String url = "content-delivery-service/content";
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put(AUTHORIZATION_HEADER, accessToken);
        headerParameterMap.put(CONTENT_TYPE_HEADER_KEY, MediaType.MULTIPART_FORM_DATA_VALUE);
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("contents", contents);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap, null, bodyMap);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (List<String>) responseEntity.getBody().getResponseBody().get("urlList");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling CONTENT-DELIVERY-SERVICE!");
        }
    }
}
