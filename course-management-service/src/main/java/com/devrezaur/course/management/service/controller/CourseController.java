package com.devrezaur.course.management.service.controller;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import com.devrezaur.course.management.service.model.Course;
import com.devrezaur.course.management.service.service.CourseService;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomHttpResponse> addCourse(@RequestBody Course course) {
        try {
            courseService.addCourse(course);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to add course! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.CREATED,
                Map.of("message", "Successfully added course info"));
    }

    @GetMapping
    public ResponseEntity<CustomHttpResponse> getAllCourses(@RequestParam @Nullable Integer pageNumber,
                                                            @RequestParam @Nullable Integer limit) {
        List<Course> courseList;
        try {
            courseList = courseService.getAllCourses(pageNumber, limit);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to fetch course list! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("courseList", courseList));
    }

    @PostMapping("/courses")
    public ResponseEntity<CustomHttpResponse> getCourseByIds(@RequestBody Map<String, Object> courseIdsMap) {
        List<Course> courseList;
        try {
            List<UUID> courseIds = ((List<String>) courseIdsMap.get("courseIds")).stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toList());
            courseList = courseService.getCourses(courseIds);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to fetch course list! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("courseList", courseList));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CustomHttpResponse> getCourseById(@PathVariable UUID courseId) {
        Course course = courseService.getCourseByCourseId(courseId);
        if (course == null) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.NOT_FOUND, "404",
                    "No course found for this course id!");
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("course", course));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomHttpResponse> updateCourse(@RequestBody Course course) {
        try {
            courseService.updateCourse(course);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to update course! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.CREATED,
                Map.of("message", "Successfully updated course info"));
    }
}
