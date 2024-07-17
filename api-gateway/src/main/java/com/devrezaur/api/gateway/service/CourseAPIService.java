package com.devrezaur.api.gateway.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.common.module.util.RequestBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;
import static com.devrezaur.common.module.constant.CommonConstant.COURSE_MANAGEMENT_API_BASE_URL;

@Service
public class CourseAPIService {

    private final HttpCallLogic httpCallLogic;

    public CourseAPIService(HttpCallLogic httpCallLogic) {
        this.httpCallLogic = httpCallLogic;
    }

    public Map<String, Object> getCourseById(UUID courseId) {
        String url = COURSE_MANAGEMENT_API_BASE_URL + "/" + courseId.toString();
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.GET, url, null, null, null);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }

    public Map<String, Object> getAllCourses(Integer pageNumber, Integer limit) {
        Map<String, String> queryParameterMap = new HashMap<>();
        if (pageNumber != null) {
            queryParameterMap.put("pageNumber", pageNumber.toString());
        }
        if (limit != null) {
            queryParameterMap.put("limit", limit.toString());
        }
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.GET,
                COURSE_MANAGEMENT_API_BASE_URL, null, queryParameterMap, null);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }

    public Map<String, Object> getAllCoursesByIds(List<String> courseIds) {
        String url = COURSE_MANAGEMENT_API_BASE_URL + "/courses";
        Map<String, Object> bodyParameterMap = Map.of("courseIds", courseIds);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, null,
                null, bodyParameterMap);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }

    public Map<String, Object> addNewCourse(Map<String, Object> course, String accessToken) {
        Map<String, String> headerParameterMap = Map.of(AUTHORIZATION_HEADER, accessToken);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST,
                COURSE_MANAGEMENT_API_BASE_URL, headerParameterMap, null, course);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }

    public Map<String, Object> updateCourse(Map<String, Object> course, String accessToken) {
        String url = COURSE_MANAGEMENT_API_BASE_URL + "/update";
        Map<String, String> headerParameterMap = Map.of(AUTHORIZATION_HEADER, accessToken);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, course);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }
}
