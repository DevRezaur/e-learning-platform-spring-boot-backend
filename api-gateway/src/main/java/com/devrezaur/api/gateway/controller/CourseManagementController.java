package com.devrezaur.api.gateway.controller;

import com.devrezaur.api.gateway.service.CourseAPIService;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/course-management-api")
public class CourseManagementController {

    private final CourseAPIService courseAPIService;

    public CourseManagementController(CourseAPIService courseAPIService) {
        this.courseAPIService = courseAPIService;
    }

    @PostMapping
    public ResponseEntity<CustomHttpResponse> addCourse(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                        @RequestBody Map<String, Object> course) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.CREATED, courseAPIService.addNewCourse(course,
                accessToken));
    }

    @GetMapping
    public ResponseEntity<CustomHttpResponse> getAllCourses(@RequestParam @Nullable Integer pageNumber,
                                                            @RequestParam @Nullable Integer limit) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, courseAPIService.getAllCourses(pageNumber, limit));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CustomHttpResponse> getCourseById(@PathVariable UUID courseId) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, courseAPIService.getCourseById(courseId));
    }

    @PostMapping("/update")
    public ResponseEntity<CustomHttpResponse> updateCourse(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                           @RequestBody Map<String, Object> course) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.CREATED, courseAPIService.updateCourse(course,
                accessToken));
    }
}
