package com.devrezaur.common.module.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfiguration {

    @Bean
    public Logger apiLogger() {
        return LoggerFactory.getLogger("apiLogger");
    }

    @Bean
    public Logger systemLogger() {
        return LoggerFactory.getLogger("systemLogger");
    }
}
