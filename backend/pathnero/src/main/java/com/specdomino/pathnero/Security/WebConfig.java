package com.specdomino.pathnero.Security;

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
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("POST")
                        .allowCredentials(true);
                
                registry.addMapping("/graphiql")  // Allow GraphiQL to work as well
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST")
                        .allowCredentials(true);
            }
        };
    }
}

