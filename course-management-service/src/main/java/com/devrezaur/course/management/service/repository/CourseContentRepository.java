package com.devrezaur.course.management.service.repository;

import com.devrezaur.course.management.service.model.CourseContent;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, UUID> {

    List<CourseContent> findByCourseId(UUID courseId, PageRequest pageable);

    CourseContent findByContentId(UUID contentId);

    @Query("SELECT c.contentSequence FROM CourseContent c WHERE c.courseId = :courseId ORDER BY c.contentSequence DESC LIMIT 1")
    Integer findTopByCourseIdOrderByContentSequenceDesc(UUID courseId);
}
