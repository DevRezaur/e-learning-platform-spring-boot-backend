package com.devrezaur.api.gateway.controller;

import com.devrezaur.api.gateway.service.ContentAPIService;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/content-api")
public class ContentDeliveryController {

    private final ContentAPIService contentAPIService;

    public ContentDeliveryController(ContentAPIService contentAPIService) {
        this.contentAPIService = contentAPIService;
    }

    @GetMapping("/file-system-storage/{contentUrl}")
    public ResponseEntity<?> getContent(@PathVariable String contentUrl) {
        try {
            return contentAPIService.serveContent(contentUrl);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.NOT_FOUND, "404",
                    "Failed to fetch the content! Reason: " + ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<CustomHttpResponse> saveContents(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                           @RequestParam MultipartFile[] contents) {
        List<String> urlList;
        try {
            urlList = contentAPIService.saveContents(contents, accessToken);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to save contents in the file system! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("urlList", urlList));
    }
}
