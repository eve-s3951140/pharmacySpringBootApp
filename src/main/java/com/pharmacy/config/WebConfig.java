package com.pharmacy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

// Configuration class to enable hidden HTTP method filter for PUT, PATCH, and DELETE requests
@Configuration
public class WebConfig {

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}