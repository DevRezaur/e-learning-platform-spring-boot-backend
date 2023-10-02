package com.devrezaur.course.management.service.service;

import com.devrezaur.course.management.service.model.CourseEnrollmentInfo;
import com.devrezaur.course.management.service.repository.CourseEnrollmentInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseEnrollmentService {

    private final CourseEnrollmentInfoRepository courseEnrollmentRepository;

    public CourseEnrollmentService(CourseEnrollmentInfoRepository courseEnrollmentRepository) {
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
        return courseEnrollmentInfoList.stream().map(CourseEnrollmentInfo::getUserId).collect(Collectors.toList());
    }

    private boolean isAlreadyEnrolled(UUID userId, UUID courseId) {
        List<UUID> enrolledCourseIds = getEnrolledCourseIds(userId);
        return enrolledCourseIds.contains(courseId);
    }
}
