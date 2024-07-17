package com.devrezaur.api.gateway.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.common.module.util.RequestBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;
import static com.devrezaur.common.module.constant.CommonConstant.COURSE_CONTENT_API_BASE_URL;

@Service
public class CourseContentAPIService {

    private final HttpCallLogic httpCallLogic;

    public CourseContentAPIService(HttpCallLogic httpCallLogic) {
        this.httpCallLogic = httpCallLogic;
    }

    public Map<String, Object> getCourseContents(UUID courseId, Integer pageNumber, Integer limit,
                                                 String accessToken) {
        String url = COURSE_CONTENT_API_BASE_URL + "/" + courseId.toString();
        Map<String, String> headerParameterMap = Map.of(AUTHORIZATION_HEADER, accessToken);
        Map<String, String> queryParameterMap = new HashMap<>();
        if (pageNumber != null) {
            queryParameterMap.put("pageNumber", pageNumber.toString());
        }
        if (limit != null) {
            queryParameterMap.put("limit", limit.toString());
        }
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.GET, url, headerParameterMap,
                queryParameterMap, null);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }

    public Map<String, Object> getCourseContentsPreview(UUID courseId, Integer pageNumber, Integer limit) {
        String url = COURSE_CONTENT_API_BASE_URL + "/" + courseId.toString() + "/preview";
        Map<String, String> queryParameterMap = new HashMap<>();
        if (pageNumber != null) {
            queryParameterMap.put("pageNumber", pageNumber.toString());
        }
        if (limit != null) {
            queryParameterMap.put("limit", limit.toString());
        }
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.GET, url, null,
                queryParameterMap, null);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }
}
