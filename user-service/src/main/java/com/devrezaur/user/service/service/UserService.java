package com.devrezaur.user.service.service;

import com.devrezaur.user.service.model.Role;
import com.devrezaur.user.service.model.User;
import com.devrezaur.user.service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.devrezaur.user.service.constant.UserServiceConstant.VALID_EMAIL_REGEX;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
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
        User updatedUser = userRepository.findByUserId(user.getUserId());
        if (updatedUser == null) {
            throw new Exception("User with id - " + user.getUserId() + " not found!");
        }
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setGender(user.getGender());
        updatedUser.setDateOfBirth(user.getDateOfBirth());
        userRepository.save(updatedUser);
    }

    private boolean isEmailValid(String email) {
        return Pattern.compile(VALID_EMAIL_REGEX).matcher(email).matches();
    }

}
