package com.devrezaur.course.management.service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course_table")
public class Course {

    @Id
    @GeneratedValue
    @Column(name = "course_id")
    private UUID courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "description")
    private String description;

    @Column(name = "is_enrollment_enabled")
    private Boolean isEnrolmentEnabled;

    @Column(name = "course_fee")
    private Integer courseFee;
}
