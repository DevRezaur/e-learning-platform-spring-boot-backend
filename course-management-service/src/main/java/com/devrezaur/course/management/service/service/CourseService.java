package com.devrezaur.course.management.service.service;

import com.devrezaur.course.management.service.model.Course;
import com.devrezaur.course.management.service.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseByUserId(UUID courseId) {
        return courseRepository.findByCourseId(courseId);
    }

    public List<Course> getCourses(List<UUID> courseIds) {
        return courseRepository.findByCourseIdIn(courseIds);
    }

    public void addCourses(Course course) throws Exception {
        Course existingCourse = courseRepository.findByCourseId(course.getCourseId());
        if (existingCourse != null) {
            throw new Exception("Course with id - " + course.getCourseId() + " already exists!");
        }
        courseRepository.save(course);
    }

    public void updateCourse(Course course) throws Exception {
        Course existingCourse = courseRepository.findByCourseId(course.getCourseId());
        if (existingCourse == null) {
            throw new Exception("Course with id - " + course.getCourseId() + " not found!");
        }
        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setIsEnrollmentEnabled(course.getIsEnrollmentEnabled());
        existingCourse.setCourseFee(course.getCourseFee());
        courseRepository.save(existingCourse);
    }

}
