package com.devrezaur.api.gateway.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.common.module.util.RequestBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

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

    public Map<String, Object> updateUserData(Map<String, Object> user, String accessToken) {
        String url = USER_API_BASE_URL + "/profile";
        Map<String, String> headerParameterMap = Map.of(AUTHORIZATION_HEADER, accessToken);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, user);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }

    public Map<String, Object> updateImageUrl(String userId, String imageUrl, String accessToken) {
        String url = USER_API_BASE_URL + "/image";
        Map<String, String> headerParameterMap = Map.of(AUTHORIZATION_HEADER, accessToken);
        Map<String, Object> bodyParameterMap = Map.of("userId", userId, "imageUrl", imageUrl);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, bodyParameterMap);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }

    public Map<String, Object> updatePassword(String userId, String password, String accessToken) {
        String url = USER_API_BASE_URL + "/password";
        Map<String, String> headerParameterMap = Map.of(AUTHORIZATION_HEADER, accessToken);
        Map<String, Object> bodyParameterMap = Map.of("userId", userId, "password", password);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, bodyParameterMap);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }
}
