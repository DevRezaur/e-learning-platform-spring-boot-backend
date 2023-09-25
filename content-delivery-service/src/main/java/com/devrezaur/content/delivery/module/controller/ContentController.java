package com.devrezaur.content.delivery.module.controller;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import com.devrezaur.content.delivery.module.model.Content;
import com.devrezaur.content.delivery.module.service.ContentService;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CustomHttpResponse> saveContents(@RequestParam MultipartFile[] contents) {
        List<UUID> contentIds;
        try {
            contentIds = contentService.saveContents(contents);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to save contents in the file system! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("contentIds", contentIds));
    }

    @GetMapping
    public ResponseEntity<CustomHttpResponse> getContentsInfo(@Nullable @RequestParam ArrayList<UUID> contentIds) {
        if (contentIds == null) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Please specify at least one contentId!");
        }
        List<Content> contentList = contentService.getContentsInfo(contentIds);
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("contentList", contentList));
    }

}
