package com.github.api.service;

import com.github.api.model.dto.BranchDto;
import com.github.api.model.dto.OwnerDto;
import com.github.api.model.dto.UserRepositoryDto;
import com.github.api.model.response.UserRepositoryResponse;
import com.github.api.repository.GithubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class GitHubServiceTest {

    @Mock
    private GithubRepository githubRepository;

    @InjectMocks
    private GitHubService gitHubService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getRepositoriesFromUser_ReturnsRepositories() {
        // Setup
        OwnerDto owner = new OwnerDto("userLogin", "http://example.com");
        UserRepositoryDto repo = new UserRepositoryDto(
                1,
                "nodeId",
                "repoName",
                "fullName", owner,
                false,
                "http://example.com/repo",
                "description",
                false
        );
        BranchDto branch = new BranchDto("main", new BranchDto.CommitDto("sha123456789"));

        when(githubRepository.getRepositoriesFromUser(anyString())).thenReturn(Mono.just(List.of(repo)));
        when(githubRepository.getBranchesForRepository(anyString(), anyString())).thenReturn(Flux.just(branch));

        // Exercise
        Mono<List<UserRepositoryResponse>> result = gitHubService.getRepositoriesFromUser("userLogin");

        // Verify
        StepVerifier.create(result)
                .expectNextMatches(userRepositoryResponses -> {
                    UserRepositoryResponse response = userRepositoryResponses.get(0);
                    return response.repositoryName().equals("repoName") &&
                            response.ownerLogin().equals("userLogin") &&
                            response.branches().size() == 1 &&
                            response.branches().get(0).getName().equals("main") &&
                            response.branches().get(0).getCommit().getSha().equals("sha123456789");
                })
                .verifyComplete();
    }
}