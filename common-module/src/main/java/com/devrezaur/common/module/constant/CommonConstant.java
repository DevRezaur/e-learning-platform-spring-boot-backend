package com.devrezaur.common.module.constant;

/**
 * Constant class containing the application level common constants.
 *
 * @author Rezaur Rahman
 */
public class CommonConstant {

    /**
     * General constants
     */
    public static final String REQUEST_ID = "Request-Id";
    public static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    public static final String ACCEPT_HEADER_KEY = "Accept";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ERROR_CODE = "errorCode";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String MESSAGE = "message";

    /**
     * Service specific constants
     */
    public static final String USER_SERVICE_BASE_URL = "user-service";

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private CommonConstant() {
    }
}
