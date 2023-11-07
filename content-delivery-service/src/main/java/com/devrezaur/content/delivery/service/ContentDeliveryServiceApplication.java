package com.devrezaur.content.delivery.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The main application class for 'content-delivery-service'.
 *
 * @author Rezaur Rahman
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.devrezaur")
public class ContentDeliveryServiceApplication {

    /**
     * Main method that starts the 'content-delivery-service' application.
     *
     * @param args command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ContentDeliveryServiceApplication.class);
    }
}
