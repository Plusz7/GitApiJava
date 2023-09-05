package com.github.api.repository;

import com.github.api.model.dto.UserRepositoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Repository
public class GithubRepository {

    private final WebClient webClient;

    @Autowired
    public GithubRepository(WebClient.Builder webclientBuilder) {
        this.webClient = webclientBuilder.baseUrl("https://api.github.com/").build();
    }

    public List<UserRepositoryDto> getRepositoryFromUser(String username) {
        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(UserRepositoryDto.class)
                .onErrorResume(error -> {
            System.out.println("Error in WebClient call: " + error.getMessage());
            return Mono.empty(); // Return an empty Mono on error
        })
                .collectList()
                .doOnSuccess(repositories -> {
                    System.out.println("Received " + repositories.size() + " repositories");
                })
                .blockOptional().orElseGet(Collections::emptyList);
    }

}
