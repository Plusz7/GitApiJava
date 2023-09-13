package com.github.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Profile("dev")
    public WebClient webClient(WebClient.Builder webclientBuilder) {
        return webclientBuilder.baseUrl("https://api.github.com/").build();
    }
}
