package com.github.api.model.response;

import com.github.api.model.dto.BranchDto;

import java.util.List;

public record UserRepositoryResponse(
        String repositoryName,
        String ownerLogin,
        List<BranchDto> branches
) {
}
