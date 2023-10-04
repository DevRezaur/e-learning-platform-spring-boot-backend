package com.devrezaur.course.management.service.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.course.management.service.model.CourseEnrollmentInfo;
import com.devrezaur.course.management.service.repository.CourseEnrollmentInfoRepository;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.devrezaur.common.module.constant.CommonConstant.CONTENT_TYPE_HEADER_KEY;
import static com.devrezaur.common.module.constant.CommonConstant.REQUEST_ID;

@Service
public class CourseEnrollmentService {

    private final HttpCallLogic httpCallLogic;
    private final CourseEnrollmentInfoRepository courseEnrollmentRepository;

    public CourseEnrollmentService(HttpCallLogic httpCallLogic,
                                   CourseEnrollmentInfoRepository courseEnrollmentRepository) {
        this.httpCallLogic = httpCallLogic;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
    }

    public void enrollToCourse(CourseEnrollmentInfo courseEnrollmentInfo) throws Exception {
        if (isAlreadyEnrolled(courseEnrollmentInfo.getUserId(), courseEnrollmentInfo.getCourseId())) {
            throw new Exception("The user is already enrolled in this course!");
        }
        courseEnrollmentRepository.save(courseEnrollmentInfo);
    }

    public List<UUID> getEnrolledCourseIds(UUID userId) {
        List<CourseEnrollmentInfo> courseEnrollmentInfoList = courseEnrollmentRepository.findByUserId(userId);
        return courseEnrollmentInfoList.stream().map(CourseEnrollmentInfo::getCourseId).collect(Collectors.toList());
    }

    public List<UUID> getEnrolledUserIds(UUID courseId) {
        List<CourseEnrollmentInfo> courseEnrollmentInfoList = courseEnrollmentRepository.findByCourseId(courseId);
        return courseEnrollmentInfoList.stream().map(CourseEnrollmentInfo::getUserId).toList();
    }

    public List<Map<String, Object>> fetchEnrolledUserInformation(List<UUID> enrolledUserIds) throws Exception {
        CustomHttpRequest customHttpRequest = new CustomHttpRequest();
        customHttpRequest.setRequestId(MDC.get(REQUEST_ID));
        customHttpRequest.setMethodType(HttpMethod.POST);
        customHttpRequest.setHeaderParameterMap(Map.of(CONTENT_TYPE_HEADER_KEY, MediaType.APPLICATION_JSON_VALUE));
        customHttpRequest.setBodyMap(Map.of("userIds", enrolledUserIds));
        customHttpRequest.setUrl("http://localhost:8081/user-service/user/list");
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (List<Map<String, Object>>) responseEntity.getBody().getResponseBody().get("userList");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling USER-SERVICE!");
        }
    }

    private boolean isAlreadyEnrolled(UUID userId, UUID courseId) {
        List<UUID> enrolledCourseIds = getEnrolledCourseIds(userId);
        return enrolledCourseIds.contains(courseId);
    }
}
