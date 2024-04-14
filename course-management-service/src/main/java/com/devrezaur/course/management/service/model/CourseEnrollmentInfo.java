package com.devrezaur.course.management.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course_enrollment_info_table")
public class CourseEnrollmentInfo {

    @Id
    @GeneratedValue
    @Column(name = "enrollment_id")
    private UUID enrollmentId;

    @Column(name = "course_id")
    private UUID courseId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "status")
    private String status;
}
