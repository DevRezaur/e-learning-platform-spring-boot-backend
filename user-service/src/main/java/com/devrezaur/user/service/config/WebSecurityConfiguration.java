package com.devrezaur.user.service.config;

import com.devrezaur.common.module.util.KeycloakRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security.
 *
 * @author Rezaur Rahman
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    /**
     * Defines a custom security filter chain configuration for Spring Security.
     * This configuration specifies how to handle security aspects of the application.
     *
     * @param httpSecurity the HttpSecurity object to configure the security filters.
     * @return instance of SecurityFilterChain that defines the order and behavior of security filters.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headersConfigurer -> headersConfigurer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(resourceServerConfigurer -> resourceServerConfigurer
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(new KeycloakRoleConverter())
                        )
                )
                .build();
    }
}
