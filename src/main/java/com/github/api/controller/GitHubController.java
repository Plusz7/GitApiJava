package com.github.api.controller;

import com.github.api.model.response.UserRepositoryResponse;
import com.github.api.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubService gitHubService;

    @Autowired
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/users/{username}/repos")
    public Mono<List<UserRepositoryResponse>> getRepositories(@PathVariable String username) {
        return gitHubService.getRepositoriesFromUser(username)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    // Handle 404 for not found users with generic ResponseStatusException for broader coverage
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        return Mono.just(Map.of(
                "status", ex.getStatusCode().value(),
                "message", Objects.requireNonNull(ex.getReason())
        ));
    }
}
