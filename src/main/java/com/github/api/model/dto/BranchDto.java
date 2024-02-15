package com.github.api.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BranchDto {
    private String name; // Branch name
    private CommitDto commit; // Last commit object

    public BranchDto(String name, CommitDto commit) {
        this.name = name;
        this.commit = commit;
    }

    public static class CommitDto {
        private String sha; // SHA of the last commit

        @JsonCreator
        public CommitDto(@JsonProperty("sha") String sha) {
            this.sha = sha;
        }

        public String getSha() {
            return sha;
        }

        public void setSha(String sha) {
            this.sha = sha;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommitDto getCommit() {
        return commit;
    }

    public void setCommit(CommitDto commit) {
        this.commit = commit;
    }
}