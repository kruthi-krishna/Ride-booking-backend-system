package com.ridesharing.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rideSharingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ride-Sharing Backend System API")
                        .description("REST API documentation for the Ride-Sharing Backend System " +
                                "(user & driver management, ride booking, fare calculation, admin operations).")
                        .version("v1.0.0")
                        .contact(new Contact().name("Ride Sharing Backend").email("support@ridesharing.example.com"))
                        .license(new License().name("MIT License")));
    }
}
