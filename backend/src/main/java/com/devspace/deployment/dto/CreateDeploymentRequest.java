package com.devspace.deployment.dto;

import com.devspace.common.enums.DeploymentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDeploymentRequest {

    @NotBlank(message = "Environment is required")
    private String environment;

    @NotBlank(message = "Version is required")
    private String version;

    private String envVariables;
}
