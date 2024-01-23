package com.devrezaur.api.gateway.config;

import com.devrezaur.common.module.util.KeycloakReactiveRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * Configuration class for reactive Spring Security.
 *
 * @author Rezaur Rahman
 */
@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurityConfiguration {

    /**
     * Defines a custom security filter chain configuration for reactive Spring Security.
     * This configuration specifies how to handle security aspects of the application in a reactive environment.
     *
     * @param serverHttpSecurity object to configure the security filters for WebFlux.
     * @return instance of SecurityWebFilterChain that defines the order and behavior of security filters.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .headers(headersConfigurer -> headersConfigurer
                        .frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable)
                )
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/actuator/**").hasRole("ADMIN")
                        .anyExchange().permitAll()
                )
                .oauth2ResourceServer(resourceServerSpec -> resourceServerSpec
                        .jwt(jwtSpec -> jwtSpec
                                .jwtAuthenticationConverter(new KeycloakReactiveRoleConverter())
                        )
                )
                .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
