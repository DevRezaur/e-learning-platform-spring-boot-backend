package com.devrezaur.api.gateway.controller;

import com.devrezaur.api.gateway.service.UserAPIService;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/user-management-api")
public class UserManagementController {

    private final UserAPIService userAPIService;

    public UserManagementController(UserAPIService userAPIService) {
        this.userAPIService = userAPIService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomHttpResponse> addRegularUser(@RequestBody Map<String, Object> userData) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.CREATED, userAPIService.addRegularUser(userData));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CustomHttpResponse> getUserById(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                          @PathVariable UUID userId) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, userAPIService.getUserById(userId, accessToken));
    }

    @PostMapping("/profile")
    public ResponseEntity<CustomHttpResponse> updateProfile(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                            @RequestBody Map<String, Object> user) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, userAPIService.updateUserData(user, accessToken));
    }

    @PostMapping("/image")
    public ResponseEntity<CustomHttpResponse> updateImageUrl(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                             @RequestBody Map<String, Object> imageUrlMap) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, userAPIService.updateImageUrl(imageUrlMap,
                accessToken));
    }

    @PostMapping("/password")
    public ResponseEntity<CustomHttpResponse> updatePassword(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                             @RequestBody Map<String, Object> passwordMap) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, userAPIService.updatePassword(passwordMap,
                accessToken));
    }
}
