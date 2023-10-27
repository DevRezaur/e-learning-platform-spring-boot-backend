package com.devrezaur.user.service.controller;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import com.devrezaur.user.service.model.Role;
import com.devrezaur.user.service.model.User;
import com.devrezaur.user.service.service.KeycloakService;
import com.devrezaur.user.service.service.UserService;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final KeycloakService keycloakService;
    private final UserService userService;

    public UserController(KeycloakService keycloakService, UserService userService) {
        this.keycloakService = keycloakService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CustomHttpResponse> addRegularUser(@RequestBody User user) {
        try {
            userService.validateEmail(user.getEmail());
            userService.validatePassword(user.getPassword());
            user.setRole(Role.USER);
            UUID userId = keycloakService.registerNewUser(user);
            user.setUserId(userId);
            userService.addUser(user);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to add user! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.CREATED, Map.of("message", "Successfully added user"));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomHttpResponse> addAdminUser(@RequestBody User user) {
        try {
            userService.validateEmail(user.getEmail());
            userService.validatePassword(user.getPassword());
            user.setRole(Role.ADMIN);
            UUID userId = keycloakService.registerNewUser(user);
            user.setUserId(userId);
            userService.addUser(user);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to add admin user! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.CREATED,
                Map.of("message", "Successfully added admin user"));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CustomHttpResponse> getUserById(@PathVariable UUID userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.NOT_FOUND, "404",
                    "No user found for this user id!");
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("user", user));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CustomHttpResponse> getAllRegularUser() {
        List<UUID> userIds = keycloakService.getUserIdsByRole(Role.USER);
        List<User> userList = userService.getListOfUser(userIds);
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("userList", userList));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomHttpResponse> getAllAdminUser() {
        List<UUID> userIds = keycloakService.getUserIdsByRole(Role.ADMIN);
        List<User> userList = userService.getListOfUser(userIds);
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("userList", userList));
    }

    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CustomHttpResponse> getListOfUser(@RequestBody Map<String, List<UUID>> userIdsMap) {
        List<User> userList;
        try {
            userList = userService.getListOfUser(userIdsMap.get("userIds"));
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to fetch user information! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("userList", userList));
    }

    @PostMapping("/profile")
    @PreAuthorize("hasRole('ADMIN') or #user.userId.toString() == authentication.principal.subject")
    public ResponseEntity<CustomHttpResponse> updateProfile(@RequestBody User user) {
        try {
            keycloakService.updateUser(user);
            userService.updateUser(user);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.EXPECTATION_FAILED, "417",
                    "Failed to update user information! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("message",
                "Successfully updated user information"));
    }

    @PostMapping("/image/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.subject")
    public ResponseEntity<CustomHttpResponse> updatePhoto(@PathVariable UUID userId,
                                                          @RequestParam MultipartFile image) {
        try {
            userService.updateProfileImage(userId, image);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.EXPECTATION_FAILED, "417",
                    "Failed to update profile image! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("message",
                "Successfully updated profile image"));
    }

    @PostMapping("/password/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.subject")
    public ResponseEntity<CustomHttpResponse> updatePassword(@PathVariable UUID userId,
                                                             @RequestBody Map<String, String> passwordMap) {
        try {
            String password = passwordMap.get("password");
            userService.validatePassword(password);
            UserResource userResource = keycloakService.getUserResourceById(userId.toString());
            keycloakService.updateUserCredentials(userResource, password);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.EXPECTATION_FAILED, "417",
                    "Failed to update password! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("message",
                "Successfully updated password"));
    }

}
