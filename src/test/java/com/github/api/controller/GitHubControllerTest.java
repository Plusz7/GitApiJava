package com.github.api.controller;

import com.github.api.model.dto.BranchDto;
import com.github.api.model.dto.OwnerDto;
import com.github.api.model.response.UserRepositoryResponse;
import com.github.api.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(GitHubController.class)
@ActiveProfiles("test")
public class GitHubControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GitHubService gitHubService;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void getRepositories_UserExists_ReturnsRepositoryList() {
        // Given
        String username = "testUser";
        OwnerDto owner = new OwnerDto("testUser", "http://example.com");
        List<BranchDto> branches = Collections.singletonList(new BranchDto("main", new BranchDto.CommitDto("sha123")));
        UserRepositoryResponse expectedResponse = new UserRepositoryResponse("testRepo", "testUser", branches);

        Mockito.when(gitHubService.getRepositoriesFromUser(username))
                .thenReturn(Mono.just(Collections.singletonList(expectedResponse)));

        // When & Then
        webTestClient.get().uri("/api/github/users/{username}/repos", username)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserRepositoryResponse.class)
                .hasSize(1)
                .consumeWith(response -> {
                    List<UserRepositoryResponse> responseBody = response.getResponseBody();
                    assertThat(responseBody).usingRecursiveComparison().isEqualTo(Collections.singletonList(expectedResponse));
                });
    }

    @Test
    public void getRepositories_UserNotFound_ReturnsNotFound() {
        // Given
        String username = "nonExistingUser";

        Mockito.when(gitHubService.getRepositoriesFromUser(username))
                .thenReturn(Mono.empty());

        // When & Then
        webTestClient.get().uri("/api/github/users/{username}/repos", username)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.message").isEqualTo("User not found");
    }
}