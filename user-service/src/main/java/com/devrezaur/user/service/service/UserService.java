package com.devrezaur.user.service.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.user.service.model.Role;
import com.devrezaur.user.service.model.User;
import com.devrezaur.user.service.repository.UserRepository;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.devrezaur.common.module.constant.CommonConstant.CONTENT_TYPE_HEADER_KEY;
import static com.devrezaur.common.module.constant.CommonConstant.REQUEST_ID;
import static com.devrezaur.user.service.constant.UserServiceConstant.VALID_EMAIL_REGEX;

@Service
public class UserService {

    private final HttpCallLogic httpCallLogic;
    private final UserRepository userRepository;

    public UserService(HttpCallLogic httpCallLogic, UserRepository userRepository) {
        this.httpCallLogic = httpCallLogic;
        this.userRepository = userRepository;
    }

    public User getUser(UUID userId) {
        return userRepository.findByUserId(userId);
    }

    public List<User> getAllRegularUser() {
        return userRepository.findByRole(Role.USER);
    }

    public List<User> getAllAdminUser() {
        return userRepository.findByRole(Role.ADMIN);
    }

    public void addUser(User user) throws Exception {
        boolean isEmailValid = isEmailValid(user.getEmail());
        if (!isEmailValid) {
            throw new Exception("Email id - " + user.getEmail() + " is not valid!");
        }
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new Exception("User with email id - " + user.getEmail() + " already exists!");
        }
        userRepository.save(user);
    }

    public void updateUser(User user) throws Exception {
        User existingUser = userRepository.findByUserId(user.getUserId());
        if (existingUser == null) {
            throw new Exception("User with id - " + user.getUserId() + " not found!");
        }
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setGender(user.getGender());
        existingUser.setDateOfBirth(user.getDateOfBirth());
        userRepository.save(existingUser);
    }

    public void updateProfileImage(UUID userId, MultipartFile image) throws Exception {
        User existingUser = userRepository.findByUserId(userId);
        if (existingUser == null) {
            throw new Exception("User with id - " + userId + " not found!");
        }
        CustomHttpRequest customHttpRequest = new CustomHttpRequest();
        customHttpRequest.setRequestId(MDC.get(REQUEST_ID));
        customHttpRequest.setMethodType(HttpMethod.POST);
        customHttpRequest.setHeaderParameterMap(Map.of(CONTENT_TYPE_HEADER_KEY, MediaType.MULTIPART_FORM_DATA_VALUE));
        customHttpRequest.setBodyMap(Map.of("contents", image.getResource()));
        customHttpRequest.setUrl("http://localhost:8082/content-delivery-service/content");
        ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<String> urlIds = (List<String>) responseEntity.getBody().getResponseBody().get("urlList");
            existingUser.setImageUrl(urlIds.get(0));
            userRepository.save(existingUser);
        }
    }

    private boolean isEmailValid(String email) {
        return Pattern.compile(VALID_EMAIL_REGEX).matcher(email).matches();
    }

}
