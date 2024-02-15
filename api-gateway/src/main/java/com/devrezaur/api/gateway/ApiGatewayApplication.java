package com.devrezaur.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The main application class for 'api-gateway'.
 *
 * @author Rezaur Rahman
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.devrezaur")
public class ApiGatewayApplication {

    /**
     * Main method that starts the 'api-gateway' application.
     *
     * @param args command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
