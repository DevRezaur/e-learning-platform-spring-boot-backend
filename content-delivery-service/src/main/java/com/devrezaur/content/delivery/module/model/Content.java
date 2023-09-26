package com.devrezaur.content.delivery.module.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "content_table")
public class Content {

    @Id
    @GeneratedValue
    @Column(name = "content_id")
    private UUID contentId;

    @Column(name = "content_name")
    private String contentName;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "extension")
    private String extension;

    @Column(name = "size")
    private long size;

    @Column(name = "hash")
    private String hash;
}
