package com.github.api.model.response;

public record UserRespositoryResponse(
        String repositoryName,
        String ownerLogin
) {
}
