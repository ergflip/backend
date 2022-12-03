package com.ergflip.ergflipbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/receiveBet").allowedOrigins("https://www.ergflip.com/");
        registry.addMapping("/backendReachable").allowedOrigins("https://www.ergflip.com/");
        registry.addMapping("/biggestSingleWinners").allowedOrigins("https://www.ergflip.com/");
        registry.addMapping("/biggestWinners").allowedOrigins("https://www.ergflip.com/");
    }
}