package com.devrezaur.course.management.service.service;

import com.devrezaur.course.management.service.model.CourseContent;
import com.devrezaur.course.management.service.repository.CourseContentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseContentService {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_LIMIT = 100;

    private final CourseContentRepository courseContentRepository;

    public CourseContentService(CourseContentRepository courseContentRepository) {
        this.courseContentRepository = courseContentRepository;
    }

    public List<CourseContent> getAllCourseContentsByCourseId(UUID courseId, Integer pageNumber, Integer limit) {
        pageNumber = Optional.ofNullable(pageNumber).orElse(DEFAULT_PAGE_NUMBER);
        limit = Optional.ofNullable(limit).orElse(DEFAULT_LIMIT);
        PageRequest pageRequest = PageRequest.of(pageNumber, limit, Sort.by("contentSequence").ascending());
        return courseContentRepository.findByCourseId(courseId, pageRequest);
    }

    public List<CourseContent> getAllCourseContentsPreviewByCourseId(UUID courseId, Integer pageNumber, Integer limit) {
        List<CourseContent> courseContents = getAllCourseContentsByCourseId(courseId, pageNumber, limit);
        courseContents.forEach(courseContent -> {
            courseContent.setContentType(null);
            courseContent.setContentUrl(null);
        });
        return courseContents;
    }

    public CourseContent getCourseContentByContentId(UUID contentId) {
        return courseContentRepository.findByContentId(contentId);
    }

    public void addCourseContent(CourseContent courseContent) {
        courseContentRepository.save(courseContent);
    }

    public void updateCourseContent(CourseContent courseContent) throws Exception {
        CourseContent existingCourseContent = courseContentRepository.findByContentId(courseContent.getContentId());
        if (existingCourseContent == null) {
            throw new Exception("Course content with id - " + courseContent.getContentId() + " not found!");
        }
        existingCourseContent.setContentTitle(courseContent.getContentTitle());
        existingCourseContent.setContentType(courseContent.getContentType());
        existingCourseContent.setContentUrl(courseContent.getContentUrl());
        existingCourseContent.setContentSequence(courseContent.getContentSequence());
        courseContentRepository.save(existingCourseContent);
    }

}
