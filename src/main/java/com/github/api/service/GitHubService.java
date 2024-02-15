package com.github.api.service;

import com.github.api.model.response.UserRepositoryResponse;
import com.github.api.repository.GithubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GitHubService {

    private final GithubRepository githubRepository;

    @Autowired
    public GitHubService(GithubRepository githubRepository) {
        this.githubRepository = githubRepository;
    }

    public Mono<List<UserRepositoryResponse>> getRepositoriesFromUser(String username) {
        return githubRepository.getRepositoriesFromUser(username)
                .flatMapMany(Flux::fromIterable)
                .flatMap(repo ->
                        githubRepository.getBranchesForRepository(
                                        repo.owner().login(),
                                        repo.name()
                                )
                                .collectList()
                                .map(branches ->
                                        new UserRepositoryResponse(
                                                repo.name(),
                                                repo.owner().login(),
                                                branches)
                                )
                )
                .collectList();
    }
}
