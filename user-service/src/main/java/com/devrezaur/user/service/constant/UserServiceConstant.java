package com.devrezaur.user.service.constant;

/**
 * Utility class containing the common constants of the application 'user-service'.
 *
 * @author Rezaur Rahman
 */
public class UserServiceConstant {

    /**
     * Constant for the standard user role.
     * This role typically permits access to user-specific resources and actions within the application.
     */
    public static final String ROLE_USER = "USER";

    /**
     * Constant for the admin role.
     * This role grants access to administrative resources and actions within the application.
     */
    public static final String ROLE_ADMIN = "ADMIN";

    /**
     * Regular expression for validating email addresses.
     * The pattern enforces a basic structure for email addresses with proper domain extensions.
     */
    public static final String VALID_EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.a-zA-Z0-9_+&*-]+)" +
            "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /**
     * Regular expression for validating passwords.
     * The pattern enforces password complexity requirements, including at least one uppercase letter,
     * one lowercase letter, one digit, and one special character, with a minimum length of 8 characters.
     */
    public static final String VALID_PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private UserServiceConstant() {
    }
}
