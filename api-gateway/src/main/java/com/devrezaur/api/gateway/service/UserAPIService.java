package com.devrezaur.api.gateway.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.common.module.util.RequestBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;
import static com.devrezaur.common.module.constant.CommonConstant.USER_API_BASE_URL;

@Service
public class UserAPIService {

    private final HttpCallLogic httpCallLogic;

    public UserAPIService(HttpCallLogic httpCallLogic) {
        this.httpCallLogic = httpCallLogic;
    }

    public Map<String, Object> addRegularUser(Map<String, Object> userData) {
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, USER_API_BASE_URL,
                null, null, userData);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }

    public Map<String, Object> getUserById(UUID userId, String accessToken) {
        String url = USER_API_BASE_URL + "/" + userId.toString();
        Map<String, String> headerParameterMap = Map.of(AUTHORIZATION_HEADER, accessToken);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.GET, url, headerParameterMap,
                null, null);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }

    public String updateUserData(Map<String, Object> user, String accessToken) throws Exception {
        String url = "user-service/user/profile";
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put(AUTHORIZATION_HEADER, accessToken);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, user);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (String) responseEntity.getBody().getResponseBody().get("message");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling USER-SERVICE!");
        }
    }

    public String updateUserPhoto(UUID userId, String imageUrl, String accessToken) throws Exception {
        String url = "user-service/user/image";
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put(AUTHORIZATION_HEADER, accessToken);
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("userId", userId);
        bodyMap.put("imageUrl", imageUrl);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, bodyMap);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (String) responseEntity.getBody().getResponseBody().get("message");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling USER-SERVICE!");
        }
    }

    public String updatePassword(String userId, String password, String accessToken) throws Exception {
        String url = "user-service/user/password";
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put(AUTHORIZATION_HEADER, accessToken);
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("userId", userId);
        bodyMap.put("password", password);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, bodyMap);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (String) responseEntity.getBody().getResponseBody().get("message");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling USER-SERVICE!");
        }
    }
}
