package com.github.api.repository;

import com.github.api.model.dto.BranchDto;
import com.github.api.model.dto.UserRepositoryDto;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Repository
public class GithubRepository {

    private final WebClient webClient;

    public GithubRepository(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<UserRepositoryDto>> getRepositoriesFromUser(String username) {
        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(UserRepositoryDto.class)
                .filter(repo -> !repo.fork()) // Filter out forks
                .collectList();
    }

    public Flux<BranchDto> getBranchesForRepository(String owner, String repo) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}/branches", owner, repo)
                .retrieve()
                .bodyToFlux(BranchDto.class)
                .onErrorResume(error -> Flux.empty()); // Handle error by returning an empty Flux
    }
}
