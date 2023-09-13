package com.github.api.config;

import okhttp3.mockwebserver.MockWebServer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class TestConfig {

    @Bean
    public MockWebServer mockWebServer() {
        return new MockWebServer();
    }

    @Bean
    @Profile("test")
    public WebClient webClient(MockWebServer mockWebServer) {
        return WebClient.builder().baseUrl(mockWebServer.getHostName()).build();
    }
}
