package com.devrezaur.common.module.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.REQUEST_ID;

@Component
public class CustomRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getHeader(REQUEST_ID) == null) {
            response.addHeader(REQUEST_ID, UUID.randomUUID().toString());
        } else {
            response.addHeader(REQUEST_ID, request.getHeader(REQUEST_ID));
        }
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long timeTaken = System.currentTimeMillis() - startTime;
        logRequestResponse(request, response, timeTaken, requestWrapper, responseWrapper);
        responseWrapper.copyBodyToResponse();
    }

    private void logRequestResponse(HttpServletRequest request, HttpServletResponse response, long timeTaken,
                                    ContentCachingRequestWrapper requestWrapper,
                                    ContentCachingResponseWrapper responseWrapper) {
        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getStringValue(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());
        LOGGER.info("FINISHED PROCESSING: REQUEST_ID={}, METHOD={}, REQUEST_URI={}, REQUEST_PAYLOAD={}, " +
                        "RESPONSE_CODE={}, RESPONSE={}, TIME_TAKEN={}ms", response.getHeader(REQUEST_ID),
                request.getMethod(), request.getRequestURI(), removeWhiteSpaceFromJsonString(requestBody),
                response.getStatus(), removeWhiteSpaceFromJsonString(responseBody), timeTaken);
    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, characterEncoding);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("Exception occurred while parsing request/response body!");
        }
        return "";
    }

    private String removeWhiteSpaceFromJsonString(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Exception occurred while parsing json string!");
        }
        return "";
    }
}
