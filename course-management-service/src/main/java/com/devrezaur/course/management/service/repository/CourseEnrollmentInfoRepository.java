package com.devrezaur.course.management.service.repository;

import com.devrezaur.course.management.service.model.CourseEnrollmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseEnrollmentInfoRepository extends JpaRepository<CourseEnrollmentInfo, UUID> {

    List<CourseEnrollmentInfo> findByUserId(UUID userId);

    List<CourseEnrollmentInfo> findByCourseId(UUID courseId);

    CourseEnrollmentInfo findByCourseIdAndUserId(UUID courseId, UUID userId);
}
