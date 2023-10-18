package com.devrezaur.user.service.constant;

public class UserServiceConstant {

    public static final String VALID_EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.a-zA-Z0-9_+&*-]+)" +
            "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static final String VALID_PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

    private UserServiceConstant() {
    }
}
