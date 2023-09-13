package com.github.api.service;

import com.github.api.model.dto.UserRepositoryDto;
import com.github.api.model.response.UserRepositoryResponse;
import com.github.api.repository.GithubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GitHubService {

    private final GithubRepository githubRepository;

    @Autowired
    public GitHubService(GithubRepository githubRepository) {
        this.githubRepository = githubRepository;
    }

    public List<UserRepositoryResponse> getRepositoriesFromUser(String username) {
        List<UserRepositoryDto> listOfRepositories = githubRepository.getRepositoryFromUser(username);
        if(listOfRepositories.isEmpty()) return Collections.emptyList();

        return listOfRepositories.stream().map(repo -> {
            String repositoryName = repo.name();
            String ownerLogin = repo.owner().login();

            return new UserRepositoryResponse(repositoryName, ownerLogin);
        }).toList();
    }
}
