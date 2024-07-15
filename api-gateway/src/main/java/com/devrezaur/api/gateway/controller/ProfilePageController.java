package com.devrezaur.api.gateway.controller;

import com.devrezaur.api.gateway.service.ContentAPIService;
import com.devrezaur.api.gateway.service.UserAPIService;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, userAPIService.getUserById(userId, accessToken));
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

    @PostMapping("/password")
    public ResponseEntity<CustomHttpResponse> updatePassword(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                             @RequestBody Map<String, String> passwordMap) {
        String message;
        try {
            message = userAPIService.updatePassword(passwordMap.get("userId"), passwordMap.get("password"), accessToken);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.EXPECTATION_FAILED, "417",
                    "Failed to update password! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("message", message));
    }
}
