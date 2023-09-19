package com.devrezaur.common.module.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.REQUEST_ID;

@Component
public class CustomRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!response.containsHeader(REQUEST_ID)) {
            response.addHeader(REQUEST_ID, UUID.randomUUID().toString());
        }
        filterChain.doFilter(request, response);
    }
}
