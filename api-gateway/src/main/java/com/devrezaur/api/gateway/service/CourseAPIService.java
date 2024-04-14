package com.devrezaur.api.gateway.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.common.module.util.RequestBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;

@Service
public class CourseAPIService {

    private final HttpCallLogic httpCallLogic;

    public CourseAPIService(HttpCallLogic httpCallLogic) {
        this.httpCallLogic = httpCallLogic;
    }

    public Map<String, Object> getCourseById(UUID courseId) throws Exception {
        String url = "course-management-service/course/{courseId}";
        Map<String, String> urlParameterMap = new HashMap<>();
        urlParameterMap.put("courseId", String.valueOf(courseId));
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.GET, url, null,
                urlParameterMap, null);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (Map<String, Object>) responseEntity.getBody().getResponseBody().get("course");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling COURSE-MANAGEMENT-SERVICE!");
        }
    }

    public List<Map<String, Object>> getAllCourses(Integer pageNumber, Integer limit) throws Exception {
        String url = "course-management-service/course";
        Map<String, String> urlParameterMap = new HashMap<>();
        if (pageNumber != null) {
            urlParameterMap.put("pageNumber", pageNumber.toString());
        }
        if (limit != null) {
            urlParameterMap.put("limit", limit.toString());
        }
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.GET, url, null,
                urlParameterMap, null);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (List<Map<String, Object>>) responseEntity.getBody().getResponseBody().get("courseList");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling COURSE-MANAGEMENT-SERVICE!");
        }
    }

    public List<Map<String, Object>> getAllCoursesByIds(List<String> courseIds) throws Exception {
        String url = "course-management-service/course/courses";
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("courseIds", courseIds);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, null,
                null, bodyMap);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (List<Map<String, Object>>) responseEntity.getBody().getResponseBody().get("courseList");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling COURSE-MANAGEMENT-SERVICE!");
        }
    }

    public String createNewCourse(Map<String, Object> course, String accessToken) throws Exception {
        String url = "course-management-service/course";
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put(AUTHORIZATION_HEADER, accessToken);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, course);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (String) responseEntity.getBody().getResponseBody().get("message");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling COURSE-MANAGEMENT-SERVICE!");
        }
    }

    public String updateCourse(Map<String, Object> course, String accessToken) throws Exception {
        String url = "course-management-service/course/update";
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put(AUTHORIZATION_HEADER, accessToken);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, course);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (String) responseEntity.getBody().getResponseBody().get("message");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling COURSE-MANAGEMENT-SERVICE!");
        }
    }
}
