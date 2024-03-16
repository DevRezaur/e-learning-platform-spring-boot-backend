package com.devrezaur.api.gateway.controller;

import com.devrezaur.api.gateway.service.CourseAPIService;
import com.devrezaur.api.gateway.service.CourseContentAPIService;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/course-page-api")
public class CoursePageController {

    private final CourseAPIService courseAPIService;
    private final CourseContentAPIService courseContentAPIService;

    public CoursePageController(CourseAPIService courseAPIService, CourseContentAPIService courseContentAPIService) {
        this.courseAPIService = courseAPIService;
        this.courseContentAPIService = courseContentAPIService;
    }

    @GetMapping
    public ResponseEntity<CustomHttpResponse> getCourseList(@RequestParam @Nullable Integer pageNumber,
                                                            @RequestParam @Nullable Integer limit) {
        List<Map<String, Object>> courseList;
        try {
            courseList = courseAPIService.getAllCourses(pageNumber, limit);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to fetch course list! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("courseList", courseList));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CustomHttpResponse> getCourseById(@PathVariable UUID courseId) {
        Map<String, Object> course;
        try {
            course = courseAPIService.getCourseById(courseId);
        } catch (Exception e) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.NOT_FOUND, "404",
                    "No course found for this course id!");
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("course", course));
    }

    @GetMapping("/{courseId}/details")
    public ResponseEntity<CustomHttpResponse> getCourseDetailsById(@PathVariable UUID courseId) {
        Map<String, Object> course;
        try {
            course = courseAPIService.getCourseById(courseId);
            List<Map<String, Object>> courseContents = courseContentAPIService.getCourseContents(courseId,
                    null, null);
            course.put("courseContents", courseContents);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.NOT_FOUND, "404",
                    "No course found for this course id!");
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("courseDetails", course));
    }

    @PostMapping
    public ResponseEntity<CustomHttpResponse> addCourse(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                        @RequestBody Map<String, Object> course) {
        String message;
        try {
            message = courseAPIService.createNewCourse(course, accessToken);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to add course! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.CREATED, Map.of("message", message));
    }

    @PostMapping("/update")
    public ResponseEntity<CustomHttpResponse> updateCourse(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                           @RequestBody Map<String, Object> course) {
        String message;
        try {
            message = courseAPIService.updateCourse(course, accessToken);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to update course! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.CREATED, Map.of("message", message));
    }

}
