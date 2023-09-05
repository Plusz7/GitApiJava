package com.github.api.model.dto;

public record UserRepositoryDto(
        int id,
        String node_id,
        String name,
        String full_name,
        OwnerDto owner,
        Boolean isPrivate,
        String html_url,
        String description
) {

}
