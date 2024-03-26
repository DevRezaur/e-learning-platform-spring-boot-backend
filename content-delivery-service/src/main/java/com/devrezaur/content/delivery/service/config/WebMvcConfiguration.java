package com.devrezaur.content.delivery.service.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Spring MVC configuration.
 *
 * @author Rezaur Rahman
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Value("${content.file-upload-path}")
    private String fileUploadPath;

    private static final String RESOURCE_HANDLER_PREFIX = "/file-system-storage/**";

    @Override
    @SneakyThrows
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Resource resource = new FileSystemResource(fileUploadPath);
        String storagePath = resource.getURL().toString();
        registry.addResourceHandler(RESOURCE_HANDLER_PREFIX)
                .addResourceLocations(storagePath);
    }
}
