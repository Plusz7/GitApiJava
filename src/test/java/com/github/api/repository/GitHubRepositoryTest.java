package com.github.api.repository;

import com.github.api.model.dto.BranchDto;
import com.github.api.model.dto.OwnerDto;
import com.github.api.model.dto.UserRepositoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

public class GitHubRepositoryTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private GithubRepository githubRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        githubRepository = new GithubRepository(webClient);
    }

    @Test
    void getRepositoriesFromUserTest() {
        String username = "testUser";
        UserRepositoryDto mockRepo = new UserRepositoryDto(
                1,
                "node_id",
                "testRepo",
                "testUser/testRepo",
                new OwnerDto("testUser", "url"),
                false,
                "html_url",
                "description",
                false);

        when(requestHeadersUriSpec.uri("/users/{username}/repos", username)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(UserRepositoryDto.class)).thenReturn(Flux.just(mockRepo));

        StepVerifier.create(githubRepository.getRepositoriesFromUser(username))
                .expectNext(List.of(mockRepo))
                .verifyComplete();
    }

    @Test
    void getBranchesForRepositoryTest() {
        String owner = "testUser";
        String repo = "testRepo";
        BranchDto mockBranch = new BranchDto("main", new BranchDto.CommitDto("sha"));

        when(requestHeadersUriSpec.uri("/repos/{owner}/{repo}/branches", owner, repo)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(BranchDto.class)).thenReturn(Flux.just(mockBranch));

        StepVerifier.create(githubRepository.getBranchesForRepository(owner, repo))
                .expectNextMatches(branch -> "main".equals(branch.getName()) && "sha".equals(branch.getCommit().getSha()))
                .verifyComplete();
    }
}