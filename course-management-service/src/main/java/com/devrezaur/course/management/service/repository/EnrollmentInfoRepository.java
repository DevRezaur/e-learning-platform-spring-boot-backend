package com.devrezaur.course.management.service.repository;

import com.devrezaur.course.management.service.model.EnrollmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnrollmentInfoRepository extends JpaRepository<EnrollmentInfo, UUID> {

    List<EnrollmentInfo> findByUserId(UUID userId);

    List<EnrollmentInfo> findByCourseId(UUID courseId);

    EnrollmentInfo findByCourseIdAndUserId(UUID courseId, UUID userId);
}
