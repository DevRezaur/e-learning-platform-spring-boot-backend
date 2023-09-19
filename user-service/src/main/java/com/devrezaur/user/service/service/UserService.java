package com.devrezaur.user.service.service;

import com.devrezaur.user.service.model.Role;
import com.devrezaur.user.service.model.User;
import com.devrezaur.user.service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(UUID userId) {
        return userRepository.findByUserId(userId);
    }

    public List<User> getAllUser() {
        return userRepository.findByRole(Role.USER);
    }

    public void addUser(User user) {
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

}
