package com.devrezaur.discovery.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * The main application class for 'discovery-server'.
 * <p>
 * This class is responsible for configuring and starting the Eureka server.
 *
 * @author Rezaur Rahman
 */
@EnableEurekaServer
@SpringBootApplication(scanBasePackages = "com.devrezaur")
public class DiscoveryServerApplication {

    /**
     * Main method that starts the 'discovery-server' application.
     *
     * @param args command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class);
    }
}
