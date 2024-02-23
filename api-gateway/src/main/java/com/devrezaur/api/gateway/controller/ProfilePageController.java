package com.devrezaur.api.gateway.controller;

import com.devrezaur.api.gateway.service.ContentAPIService;
import com.devrezaur.api.gateway.service.UserAPIService;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/profile-page-api")
public class ProfilePageController {

    private final UserAPIService userAPIService;
    private final ContentAPIService contentAPIService;

    public ProfilePageController(UserAPIService userAPIService, ContentAPIService contentAPIService) {
        this.userAPIService = userAPIService;
        this.contentAPIService = contentAPIService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CustomHttpResponse> getUserData(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                          @PathVariable UUID userId) {
        Map<String, Object> user;
        try {
            user = userAPIService.getUserById(userId, accessToken);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.NOT_FOUND, "404",
                    "Failed to fetch user data! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("user", user));
    }

    @PostMapping
    public ResponseEntity<CustomHttpResponse> updateProfile(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                            @RequestBody Map<String, Object> user) {
        String message;
        try {
            message = userAPIService.updateUserData(user, accessToken);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.EXPECTATION_FAILED, "417",
                    "Failed to update user data! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("message", message));
    }

    @PostMapping("image/{userId}")
    public ResponseEntity<CustomHttpResponse> updatePhoto(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                          @PathVariable UUID userId,
                                                          @RequestParam MultipartFile image) {
        String message;
        try {
            List<String> urlList = contentAPIService.saveContents(new MultipartFile[]{image}, accessToken);
            message = userAPIService.updateUserPhoto(userId, urlList.get(0), accessToken);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to update user photo! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("message", message));
    }
}
