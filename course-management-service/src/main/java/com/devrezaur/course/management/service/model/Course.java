package com.devrezaur.course.management.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
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

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_enrollment_enabled")
    private Boolean isEnrollmentEnabled;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Transient
    private String enrollmentStatus;

    @Column(name = "course_fee")
    private Integer courseFee;

    @Column(name = "discount")
    private Integer discount;

    @CreationTimestamp
    @Column(name = "published_at", nullable = false, updatable = false)
    private Date publishedAt;
}
