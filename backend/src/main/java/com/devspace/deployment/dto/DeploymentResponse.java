package com.devspace.deployment.dto;

import com.devspace.common.enums.DeploymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class DeploymentResponse {

    private Long id;
    private Long projectId;
    private String environment;
    private String version;
    private DeploymentStatus status;
    private String logs;
    private String envVariables;
    private Instant createdAt;
    private Instant updatedAt;
}
