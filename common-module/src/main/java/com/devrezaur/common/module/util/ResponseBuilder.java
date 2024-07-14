package com.devrezaur.common.module.util;

import com.devrezaur.common.module.model.CustomHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.CODE;
import static com.devrezaur.common.module.constant.CommonConstant.MESSAGE;

public class ResponseBuilder {

    public static ResponseEntity<CustomHttpResponse> buildSuccessResponse(HttpStatus httpStatus,
                                                                          Map<String, Object> responseBody) {
        CustomHttpResponse successResponse = CustomHttpResponse
                .builder()
                .httpStatus(httpStatus)
                .responseBody(responseBody)
                .build();
        return new ResponseEntity<>(successResponse, httpStatus);
    }

    public static ResponseEntity<CustomHttpResponse> buildFailureResponse(HttpStatus httpStatus,
                                                                          String errorCode,
                                                                          String errorMessage) {
        CustomHttpResponse errorResponse = CustomHttpResponse
                .builder()
                .httpStatus(httpStatus)
                .errorBody(Map.of(CODE, errorCode, MESSAGE, errorMessage))
                .build();
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
