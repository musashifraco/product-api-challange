package com.example.product_management_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
public class ApiKeyConfig {

    @Bean
    public DelegatingFilterProxy apiKeyDelegatingFilterProxy() {
        return new DelegatingFilterProxy("apiKeyFilter");
    }
}
