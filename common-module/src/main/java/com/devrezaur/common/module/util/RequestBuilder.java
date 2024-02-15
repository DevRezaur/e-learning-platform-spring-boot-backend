package com.devrezaur.common.module.util;

import com.devrezaur.common.module.model.CustomHttpRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;

import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.REQUEST_ID;

public class RequestBuilder {

    public static CustomHttpRequest buildRequest(HttpMethod methodType, String url,
                                                 Map<String, String> headerParameterMap,
                                                 Map<String, String> urlParameterMap,
                                                 Map<String, Object> bodyMap) {
        return CustomHttpRequest
                .builder()
                .requestId(MDC.get(REQUEST_ID))
                .methodType(methodType)
                .url("lb://" + url)
                .headerParameterMap(headerParameterMap)
                .urlParameterMap(urlParameterMap)
                .bodyMap(bodyMap)
                .build();
    }
}
