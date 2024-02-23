package com.devrezaur.user.service.service;

import com.devrezaur.user.service.model.User;
import com.devrezaur.user.service.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.devrezaur.user.service.constant.UserServiceConstant.VALID_EMAIL_REGEX;
import static com.devrezaur.user.service.constant.UserServiceConstant.VALID_PASSWORD_REGEX;

/**
 * Service class for managing the User entity.
 * <p>
 * This class provides methods for interacting with 'user-service-db'.
 *
 * @author Rezaur Rahman
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructor for UserService class.
     *
     * @param userRepository instance of UserRepository interface.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a user by their UUID.
     *
     * @param userId the UUID of the user to retrieve.
     * @return user information associated with the UUID.
     */
    public User getUser(UUID userId) {
        return userRepository.findByUserId(userId);
    }

    /**
     * Retrieves a list of users based on their UUIDs.
     *
     * @param userIds list of UUIDs for users to retrieve.
     * @return list of user information associated with the UUIDs.
     */
    public List<User> getListOfUser(List<UUID> userIds) {
        return userRepository.findByUserIdIn(userIds);
    }

    /**
     * Adds a new user to the 'user-service-db'.
     *
     * @param user the user object representing the new user to add.
     * @throws Exception if a user with the same email already exists.
     */
    public void addUser(User user) throws Exception {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new Exception("User with email id - " + user.getEmail() + " already exists!");
        }
        userRepository.save(user);
    }

    /**
     * Updates the user's information.
     *
     * @param user user data with updated information.
     * @throws Exception if the user is not found.
     */
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

    /**
     * Updates the user's profile image.
     *
     * @param userId   UUID of the user for whom to update the profile image.
     * @param imageUrl new image location url.
     * @throws Exception if the user is not found or an error occurs during the image update.
     */
    public void updateProfileImage(UUID userId, String imageUrl) throws Exception {
        User existingUser = userRepository.findByUserId(userId);
        if (existingUser == null) {
            throw new Exception("User with id - " + userId + " not found!");
        }
        existingUser.setImageUrl(imageUrl);
        userRepository.save(existingUser);
    }

    /**
     * Validates the format of an email address.
     *
     * @param email the email address that needs to be validated.
     * @throws Exception if the email is not in a valid format.
     */
    public void validateEmail(String email) throws Exception {
        boolean isEmailValid = isEmailValid(email);
        if (!isEmailValid) {
            throw new Exception("Email id - " + email + " is not valid!");
        }
    }

    /**
     * Validates the format of a password.
     *
     * @param password the password that needs to be validated.
     * @throws Exception if the password is not in a valid format.
     */
    public void validatePassword(String password) throws Exception {
        boolean isPasswordValid = isPasswordValid(password);
        if (!isPasswordValid) {
            throw new Exception("Password should be minimum eight characters. And contain at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character!");
        }
    }

    /**
     * Checks if the provided email address matches to the defined regex format.
     *
     * @param email the email address that needs to be validated.
     * @return true if it matches to the regex format. Else returns false.
     */
    private boolean isEmailValid(String email) {
        if (!StringUtils.hasLength(email)) {
            return false;
        }
        return Pattern.compile(VALID_EMAIL_REGEX).matcher(email).matches();
    }

    /**
     * Checks if the provided password matches to the defined regex format.
     *
     * @param password the password that needs to be validated.
     * @return true if it matches to the regex format. Else returns false.
     */
    private boolean isPasswordValid(String password) {
        if (!StringUtils.hasLength(password)) {
            return false;
        }
        return Pattern.compile(VALID_PASSWORD_REGEX).matcher(password).matches();
    }
}
