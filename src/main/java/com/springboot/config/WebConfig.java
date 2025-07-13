package com.springboot.config;

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
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://abroad.kozow.com",
                                "http://43.202.4.30"
                        ) // 허용할 프론트엔드 도메인
                        .allowedMethods("*") // GET, POST 등 모두 허용
                        .allowedHeaders("*"); // 모든 헤더 허용
            }
        };
    }
}
