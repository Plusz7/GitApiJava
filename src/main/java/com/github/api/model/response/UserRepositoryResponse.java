package com.github.api.model.response;

public record UserRepositoryResponse(
        String repositoryName,
        String ownerLogin
) {
}
