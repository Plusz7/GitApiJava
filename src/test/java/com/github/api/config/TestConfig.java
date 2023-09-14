package com.github.api.config;

import okhttp3.mockwebserver.MockWebServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.logging.Level;
import java.util.logging.Logger;

@TestConfiguration
public class TestConfig {

    @Bean
    public MockWebServer mockWebServer() {
        Logger logger = Logger.getLogger(MockWebServer.class.getName());
        logger.setLevel(Level.INFO); // Set the log level to INFO or desired level
        return new MockWebServer();
    }

    @Bean(name = "testing")
    @Profile("test")
    public WebClient webClient(MockWebServer mockWebServer) {
        return WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
    }
}
