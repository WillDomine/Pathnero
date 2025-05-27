package com.specdomino.pathnero;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/graphql")
                        .allowedOrigins("http://localhost:5173") // Allow your frontend's URL
                        .allowedMethods("GET", "POST", "OPTIONS") // Allow GET, POST, and OPTIONS methods
                        .allowCredentials(true)
                        .allowedHeaders("*"); // Allow all headers, or you can be specific
            }
        };
    }
}
