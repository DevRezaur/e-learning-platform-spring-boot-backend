package com.devrezaur.course.management.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "course_content_table")
public class CourseContent {

    @Id
    @GeneratedValue
    @Column(name = "content_id")
    private UUID contentId;

    @Column(name = "course_id")
    private UUID courseId;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "content_url")
    private String contentUrl;

    @Column(name = "content_sequence")
    private String contentSequence;
}
