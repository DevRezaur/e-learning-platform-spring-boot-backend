package com.devrezaur.user.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The main application class for 'user-service'.
 *
 * @author Rezaur Rahman
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.devrezaur")
public class UserServiceApplication {

    /**
     * Main method that starts the 'user-service' application.
     *
     * @param args command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class);
    }
}
