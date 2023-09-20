package com.devrezaur.content.delivery.module.controller;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.content.delivery.module.service.ContentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public CustomHttpResponse saveContents(@RequestParam MultipartFile[] contents) {
        ArrayList<String> hashList;
        try {
            hashList = contentService.saveContents(contents);
        } catch (Exception ex) {
            return CustomHttpResponse
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .customErrorCode("400")
                    .errorMessage("Failed to save contents in the file system! Reason: " + ex.getMessage())
                    .build();
        }
        return CustomHttpResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(Map.of("hashList", hashList))
                .build();
    }

}
