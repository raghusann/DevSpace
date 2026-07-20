package com.devspace.github.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GitHubConnectionResponse {

    private boolean connected;
    private String username;
    private List<GitHubRepoDto> repositories;
}
