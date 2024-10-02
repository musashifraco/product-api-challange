package com.example.product_management_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@EnableCaching
@EnableJdbcAuditing
@SpringBootApplication
public class ProductManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductManagementApiApplication.class, args);
	}

}
