package com.devspace.github.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LinkRepoRequest {

    @NotBlank(message = "Repository full name is required")
    private String repoFullName;

    @NotBlank(message = "Repository URL is required")
    private String repoUrl;

    private String defaultBranch = "main";
}
