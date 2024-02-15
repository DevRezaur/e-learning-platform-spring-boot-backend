package com.devrezaur.api.gateway.controller;

import com.devrezaur.api.gateway.service.CourseAPIService;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home-screen-api")
public class HomeScreenController {

    private final CourseAPIService courseAPIService;

    public HomeScreenController(CourseAPIService courseAPIService) {
        this.courseAPIService = courseAPIService;
    }

    @GetMapping
    public ResponseEntity<CustomHttpResponse> getHomeScreenData() {
        List<Map<String, Object>> courseList;
        try {
            courseList = courseAPIService.getAllCourses(0, 6);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to fetch course list! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("courseList", courseList));
    }
}
