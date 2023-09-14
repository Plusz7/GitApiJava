package com.github.api.controller;

import com.github.api.model.response.UserRepositoryResponse;
import com.github.api.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubService gitHubService;

    @Autowired
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/user/{username}/repos")
    public List<UserRepositoryResponse> getUserRepositories(
            @PathVariable String username,
            @RequestHeader("Accept") String acceptHeader
    ) {
        return gitHubService.getRepositoriesFromUser(username);
    }

}
