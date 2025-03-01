package com.devrezaur.course.management.service.controller;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import com.devrezaur.course.management.service.model.Course;
import com.devrezaur.course.management.service.service.CourseService;
import com.devrezaur.course.management.service.service.EnrollmentService;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.MESSAGE;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public CourseController(CourseService courseService, EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomHttpResponse> addCourse(@RequestBody Course course) {
        try {
            course = courseService.addCourse(course);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to add course! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.CREATED, Map.of(MESSAGE,
                "Successfully added course info", "course", course));
    }

    @GetMapping
    public ResponseEntity<CustomHttpResponse> getAllCourses(@RequestParam @Nullable Integer pageNumber,
                                                            @RequestParam @Nullable Integer limit) {
        Long totalCount;
        List<Course> courseList;
        try {
            totalCount = courseService.getTotalCourseCount();
            courseList = courseService.getAllCourses(pageNumber, limit);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to fetch course list! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("totalCount", totalCount,
                "courseList", courseList));
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
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of(MESSAGE,
                "Successfully updated course info"));
    }

    @GetMapping("/enrollment/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.subject")
    public ResponseEntity<CustomHttpResponse> getAllEnrolledCourses(@PathVariable UUID userId) {
        List<Course> enrolledCourseList;
        try {
            enrolledCourseList = enrollmentService.getEnrolledCourses(userId);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to fetch enrolled course list with status! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("enrolledCourseList", enrolledCourseList));
    }

    @GetMapping("/enrollment/{courseId}/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.subject")
    public ResponseEntity<CustomHttpResponse> getEnrollmentStatus(@PathVariable UUID courseId,
                                                                  @PathVariable UUID userId) {
        String status;
        try {
            status = enrollmentService.getEnrollmentStatus(courseId, userId);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.NOT_FOUND, "404",
                    "Failed to fetch enrollment status! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("status", status));
    }
}
