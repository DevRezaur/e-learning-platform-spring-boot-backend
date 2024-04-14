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

@Service
public class CourseEnrollmentAPIService {

    private final HttpCallLogic httpCallLogic;

    public CourseEnrollmentAPIService(HttpCallLogic httpCallLogic) {
        this.httpCallLogic = httpCallLogic;
    }

    public String enrollToCourse(Map<String, Object> courseEnrollmentInfoMap, String accessToken) throws Exception {
        String url = "course-management-service/course-enrollment/enroll";
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put(AUTHORIZATION_HEADER, accessToken);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, courseEnrollmentInfoMap);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (String) responseEntity.getBody().getResponseBody().get("message");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling COURSE-MANAGEMENT-SERVICE!");
        }
    }

    public Map<String, String> getAllEnrolledCourseIdsWithStatus(UUID userId, String accessToken)
            throws Exception {
        String url = "course-management-service/course-enrollment/enroll/courses";
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put(AUTHORIZATION_HEADER, accessToken);
        Map<String, String> urlParameterMap = new HashMap<>();
        urlParameterMap.put("userId", userId.toString());
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.GET, url, headerParameterMap,
                urlParameterMap, null);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (Map<String, String>) responseEntity.getBody().getResponseBody().get("enrolledCourseIdsWithStatusMap");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling COURSE-MANAGEMENT-SERVICE!");
        }
    }
}
