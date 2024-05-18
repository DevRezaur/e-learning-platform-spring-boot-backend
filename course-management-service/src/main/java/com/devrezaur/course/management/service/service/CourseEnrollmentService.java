package com.devrezaur.course.management.service.service;

import com.devrezaur.course.management.service.model.CourseEnrollmentInfo;
import com.devrezaur.course.management.service.repository.CourseEnrollmentInfoRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CourseEnrollmentService {

    private final CourseEnrollmentInfoRepository courseEnrollmentRepository;

    public CourseEnrollmentService(CourseEnrollmentInfoRepository courseEnrollmentRepository) {
        this.courseEnrollmentRepository = courseEnrollmentRepository;
    }

    public void enrollToCourse(CourseEnrollmentInfo courseEnrollmentInfo) {
        CourseEnrollmentInfo existingCourseEnrollmentInfo = courseEnrollmentRepository.findByCourseIdAndUserId(
                courseEnrollmentInfo.getCourseId(), courseEnrollmentInfo.getUserId());
        if (existingCourseEnrollmentInfo != null) {
            existingCourseEnrollmentInfo.setStatus(courseEnrollmentInfo.getStatus());
        }
        courseEnrollmentRepository.save(courseEnrollmentInfo);
    }

    public Map<UUID, String> getEnrolledCourseIdsWithStatus(UUID userId) {
        List<CourseEnrollmentInfo> courseEnrollmentInfoList = courseEnrollmentRepository.findByUserId(userId);
        Map<UUID, String> enrolledCourseIdsWithStatusMap = new HashMap<>();
        for (CourseEnrollmentInfo courseEnrollmentInfo : courseEnrollmentInfoList) {
            enrolledCourseIdsWithStatusMap.put(courseEnrollmentInfo.getCourseId(), courseEnrollmentInfo.getStatus());
        }
        return enrolledCourseIdsWithStatusMap;
    }

    public List<UUID> getEnrolledUserIds(UUID courseId) {
        List<CourseEnrollmentInfo> courseEnrollmentInfoList = courseEnrollmentRepository.findByCourseId(courseId);
        return courseEnrollmentInfoList.stream().map(CourseEnrollmentInfo::getUserId).toList();
    }
}
