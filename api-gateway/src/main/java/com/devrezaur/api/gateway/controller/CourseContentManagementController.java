package com.devrezaur.api.gateway.controller;

import com.devrezaur.api.gateway.service.CourseContentAPIService;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/course-content-management-api")
public class CourseContentManagementController {

    private final CourseContentAPIService courseContentAPIService;

    public CourseContentManagementController(CourseContentAPIService courseContentAPIService) {
        this.courseContentAPIService = courseContentAPIService;
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CustomHttpResponse> getCourseContents(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                                @PathVariable UUID courseId,
                                                                @RequestParam @Nullable Integer pageNumber,
                                                                @RequestParam @Nullable Integer limit) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, courseContentAPIService.getCourseContents(courseId,
                pageNumber, limit, accessToken));
    }

    @GetMapping("/{courseId}/preview")
    public ResponseEntity<CustomHttpResponse> getCourseContentsPreview(@PathVariable UUID courseId,
                                                                       @RequestParam @Nullable Integer pageNumber,
                                                                       @RequestParam @Nullable Integer limit) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, courseContentAPIService.getCourseContentsPreview(
                courseId, pageNumber, limit));
    }
}
