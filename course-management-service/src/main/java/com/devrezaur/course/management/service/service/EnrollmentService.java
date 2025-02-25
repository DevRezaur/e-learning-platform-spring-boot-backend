package com.devrezaur.course.management.service.service;

import com.devrezaur.course.management.service.model.Course;
import com.devrezaur.course.management.service.model.EnrollmentInfo;
import com.devrezaur.course.management.service.repository.EnrollmentInfoRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EnrollmentService {

    private final EnrollmentInfoRepository enrollmentRepository;
    private final CourseService courseService;

    public EnrollmentService(EnrollmentInfoRepository enrollmentRepository, CourseService courseService) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseService = courseService;
    }

    public void enrollToCourse(UUID courseId, UUID userId, String status) {
        EnrollmentInfo existingEnrollmentInfo = enrollmentRepository.findByCourseIdAndUserId(courseId, userId);
        if (existingEnrollmentInfo == null) {
            existingEnrollmentInfo = new EnrollmentInfo();
            existingEnrollmentInfo.setCourseId(courseId);
            existingEnrollmentInfo.setUserId(userId);
        }
        existingEnrollmentInfo.setStatus(status);
        enrollmentRepository.save(existingEnrollmentInfo);
    }

    public List<Course> getEnrolledCourses(UUID userId) {
        List<EnrollmentInfo> enrollmentInfoList = enrollmentRepository.findByUserId(userId);
        Map<UUID, String> enrolledCourseIdsWithStatusMap = new HashMap<>();
        for (EnrollmentInfo enrollmentInfo : enrollmentInfoList) {
            enrolledCourseIdsWithStatusMap.put(enrollmentInfo.getCourseId(), enrollmentInfo.getStatus());
        }
        List<UUID> enrolledCourseIds = new ArrayList<>(enrolledCourseIdsWithStatusMap.keySet());
        List<Course> enrolledCourses = courseService.getListOfCourse(enrolledCourseIds);
        for (Course enrolledCourse : enrolledCourses) {
            enrolledCourse.setEnrollmentStatus(enrolledCourseIdsWithStatusMap.get(enrolledCourse.getCourseId()));
        }
        return enrolledCourses;
    }

    public List<UUID> getEnrolledUserIds(UUID courseId) {
        List<EnrollmentInfo> courseEnrollmentInfoList = enrollmentRepository.findByCourseId(courseId);
        return courseEnrollmentInfoList.stream().map(EnrollmentInfo::getUserId).toList();
    }
}
