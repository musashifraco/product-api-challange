package com.example.product_management_api.config;

import com.example.product_management_api.entity.Product;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.List;

@Configuration
public class CacheConfig {

    @Bean
    public RedisTemplate<String, List<Product>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<Product>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}

