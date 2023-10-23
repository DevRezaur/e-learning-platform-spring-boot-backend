package com.devrezaur.user.service.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.user.service.model.User;
import com.devrezaur.user.service.repository.UserRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.devrezaur.common.module.constant.CommonConstant.CONTENT_TYPE_HEADER_KEY;
import static com.devrezaur.common.module.constant.CommonConstant.REQUEST_ID;
import static com.devrezaur.user.service.constant.UserServiceConstant.VALID_EMAIL_REGEX;
import static com.devrezaur.user.service.constant.UserServiceConstant.VALID_PASSWORD_REGEX;

@Service
public class UserService {

    @Value("${service.content-delivery-service.base-url}")
    private String contentDeliveryServiceBaseUrl;

    private final HttpCallLogic httpCallLogic;
    private final UserRepository userRepository;

    public UserService(HttpCallLogic httpCallLogic, UserRepository userRepository) {
        this.httpCallLogic = httpCallLogic;
        this.userRepository = userRepository;
    }

    public User getUser(UUID userId) {
        return userRepository.findByUserId(userId);
    }

    public List<User> getListOfUser(List<UUID> userIds) {
        return userRepository.findByUserIdIn(userIds);
    }

    public void addUser(User user) throws Exception {
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
        customHttpRequest.setUrl(contentDeliveryServiceBaseUrl + "/content");
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            List<String> urlIds = (List<String>) responseEntity.getBody().getResponseBody().get("urlList");
            existingUser.setImageUrl(urlIds.get(0));
            userRepository.save(existingUser);
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling CONTENT-DELIVERY-SERVICE!");
        }
    }

    public void validateEmail(String email) throws Exception {
        boolean isEmailValid = isEmailValid(email);
        if (!isEmailValid) {
            throw new Exception("Email id - " + email + " is not valid!");
        }
    }

    public void validatePassword(String password) throws Exception {
        boolean isPasswordValid = isPasswordValid(password);
        if (!isPasswordValid) {
            throw new Exception("Password should be minimum eight characters. And contain at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character!");
        }
    }

    private boolean isEmailValid(String email) {
        if (!StringUtils.hasLength(email)) {
            return false;
        }
        return Pattern.compile(VALID_EMAIL_REGEX).matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        if (!StringUtils.hasLength(password)) {
            return false;
        }
        return Pattern.compile(VALID_PASSWORD_REGEX).matcher(password).matches();
    }
}
