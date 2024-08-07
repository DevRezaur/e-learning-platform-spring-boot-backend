package com.devrezaur.course.management.service.controller;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import com.devrezaur.course.management.service.model.EnrollmentInfo;
import com.devrezaur.course.management.service.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.MESSAGE;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomHttpResponse> enrollToCourse(@RequestBody EnrollmentInfo enrollmentInfo) {
        try {
            enrollmentService.enrollToCourse(enrollmentInfo);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to enroll to the course! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of(MESSAGE,
                "Successfully enrolled to the course"));
    }

    @GetMapping("/courses")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CustomHttpResponse> getAllEnrolledCourseIdsWithStatus(@RequestParam UUID userId) {
        Map<UUID, String> enrolledCourseIdsWithStatusMap;
        try {
            enrolledCourseIdsWithStatusMap = enrollmentService.getEnrolledCourseIdsWithStatus(userId);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to fetch enrolled course id list with status! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("enrolledCourseIdsWithStatus",
                enrolledCourseIdsWithStatusMap));
    }
}
