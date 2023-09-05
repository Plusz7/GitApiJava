package com.github.api.controller;

import com.github.api.model.response.UserRespositoryResponse;
import com.github.api.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<UserRespositoryResponse> getUserRepositories(
            @PathVariable String username
    ) {
        return gitHubService.getRepositoriesFromUser(username);
    }

}
