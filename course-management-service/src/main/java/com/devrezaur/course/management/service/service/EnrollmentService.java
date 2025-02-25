package com.devrezaur.course.management.service.service;

import com.devrezaur.course.management.service.model.EnrollmentInfo;
import com.devrezaur.course.management.service.repository.EnrollmentInfoRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EnrollmentService {

    private final EnrollmentInfoRepository enrollmentRepository;

    public EnrollmentService(EnrollmentInfoRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
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

    public Map<UUID, String> getEnrolledCourseIdsWithStatus(UUID userId) {
        List<EnrollmentInfo> enrollmentInfoList = enrollmentRepository.findByUserId(userId);
        Map<UUID, String> enrolledCourseIdsWithStatusMap = new HashMap<>();
        for (EnrollmentInfo enrollmentInfo : enrollmentInfoList) {
            enrolledCourseIdsWithStatusMap.put(enrollmentInfo.getCourseId(), enrollmentInfo.getStatus());
        }
        return enrolledCourseIdsWithStatusMap;
    }

    public List<UUID> getEnrolledUserIds(UUID courseId) {
        List<EnrollmentInfo> courseEnrollmentInfoList = enrollmentRepository.findByCourseId(courseId);
        return courseEnrollmentInfoList.stream().map(EnrollmentInfo::getUserId).toList();
    }
}
