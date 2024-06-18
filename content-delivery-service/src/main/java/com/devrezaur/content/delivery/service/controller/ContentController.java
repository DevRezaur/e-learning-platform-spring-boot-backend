package com.devrezaur.content.delivery.service.controller;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import com.devrezaur.content.delivery.service.service.ContentService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Main controller class for the application 'content-delivery-service'.
 * <p>
 * All the APIs related to content management are written here.
 *
 * @author Rezaur Rahman
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;

    /**
     * Constructor for ContentController class.
     *
     * @param contentService instance of ContentService.
     */
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    /**
     * API to save contents to the file system storage.
     * <p>
     * To use this API, client application needs to pass access token with either role 'ADMIN' or 'USER'.
     *
     * @param contents an array of multipart file objects representing the content files to be saved.
     * @return success only if the contents can be successfully stored to the file system.
     * Else returns 400-Bad Request.
     */
    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CustomHttpResponse> saveContents(@RequestParam MultipartFile[] contents) {
        List<String> urlList;
        try {
            urlList = contentService.saveContents(contents);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to save contents in the file system! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("urlList", urlList));
    }

    @GetMapping("/stream")
    public ResponseEntity<InputStreamResource> streamVideo(@RequestHeader HttpHeaders headers)
            throws IOException {
        File videoFile = new File("file-system-storage/dummy-video-1.mp4");
        InputStream videoStream = new FileInputStream(videoFile);
        long fileSize = videoFile.length();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.CONTENT_TYPE, "video/mp4");

        if (headers.getRange().isEmpty()) {
            responseHeaders.setContentLength(fileSize);
            return new ResponseEntity<>(new InputStreamResource(videoStream), responseHeaders, HttpStatus.OK);
        } else {
            HttpRange range = headers.getRange().get(0);
            long start = range.getRangeStart(fileSize);
            long end = range.getRangeEnd(fileSize);
            responseHeaders.add(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
            responseHeaders.setContentLength(end - start + 1);
            videoStream.skip(start);
            return new ResponseEntity<>(new InputStreamResource(videoStream), responseHeaders,
                    HttpStatus.PARTIAL_CONTENT);
        }
    }

}
