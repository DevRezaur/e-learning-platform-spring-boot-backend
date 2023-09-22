package com.devrezaur.content.delivery.module.repository;

import com.devrezaur.content.delivery.module.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContentRepository extends JpaRepository<Content, UUID> {

    Content findByContentId(UUID contentId);
}
