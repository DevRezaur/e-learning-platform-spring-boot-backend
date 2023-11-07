package com.devrezaur.api.gateway;

import com.devrezaur.common.module.config.RestTemplateConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * The main application class for 'api-gateway'.
 * <p>
 * This class serves as the bootstrap for the API Gateway.
 * Which is responsible for routing requests to different microservices within the system.
 *
 * @author Rezaur Rahman
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.devrezaur")
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        RestTemplateConfiguration.class
                }
        )
})
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
