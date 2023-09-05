package com.github.api.config;

import com.github.api.repository.GitHubRepositoryTest;
import okhttp3.mockwebserver.MockWebServer;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class TestConfig {

    @Bean
    public MockWebServer mockWebServer() {
        return new MockWebServer();
    }

    @Bean
    public WebClient webClient(MockWebServer mockWebServer) {
        return WebClient.builder().baseUrl(mockWebServer.getHostName()).build();
    }
}
