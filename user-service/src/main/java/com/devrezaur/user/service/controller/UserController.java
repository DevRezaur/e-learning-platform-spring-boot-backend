package com.devrezaur.user.service.controller;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.user.service.model.Role;
import com.devrezaur.user.service.model.User;
import com.devrezaur.user.service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user-service")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{userId}")
    public CustomHttpResponse getUserById(@PathVariable UUID userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return CustomHttpResponse
                    .builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .customErrorCode("404")
                    .errorMessage("No user found for this user id!")
                    .build();
        }
        return CustomHttpResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(Map.of("user", user))
                .build();
    }

    @PostMapping("/user")
    public CustomHttpResponse addRegularUser(@RequestBody User user) {
        try {
            user.setRole(Role.USER);
            userService.addUser(user);
        } catch (Exception ex) {
            return CustomHttpResponse
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .customErrorCode("400")
                    .errorMessage("Failed to add user! Reason: " + ex.getCause())
                    .build();
        }
        return CustomHttpResponse
                .builder()
                .httpStatus(HttpStatus.CREATED)
                .responseBody(Map.of("message", "Successfully added user"))
                .build();
    }

    @PostMapping("/user/admin")
    public CustomHttpResponse addAdminUser(@RequestBody User user) {
        try {
            user.setRole(Role.ADMIN);
            userService.addUser(user);
        } catch (Exception ex) {
            return CustomHttpResponse
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .customErrorCode("400")
                    .errorMessage("Failed to add admin user! Reason: " + ex.getCause())
                    .build();
        }
        return CustomHttpResponse
                .builder()
                .httpStatus(HttpStatus.CREATED)
                .responseBody(Map.of("message", "Successfully added admin user"))
                .build();
    }

    @GetMapping("/user")
    public CustomHttpResponse getAllUser() {
        List<User> userList = userService.getAllUser();
        return CustomHttpResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(Map.of("userList", userList))
                .build();
    }

    @PostMapping("/user/image")
    public ResponseEntity<?> updatePhoto(@RequestBody UUID userId, @RequestBody String imageUrl) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("user/profile")
    public CustomHttpResponse updateProfile(@RequestBody User user) {
        try {
            userService.updateUser(user);
        } catch (Exception ex) {
            return CustomHttpResponse
                    .builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .customErrorCode("417")
                    .errorMessage("Failed to update user information! Reason: " + ex.getMessage())
                    .build();
        }
        return CustomHttpResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(Map.of("message", "Successfully updated user information"))
                .build();
    }

}
