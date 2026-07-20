package com.devspace.deployment.controller;

import com.devspace.common.ApiResponse;
import com.devspace.common.PageResponse;
import com.devspace.deployment.dto.CreateDeploymentRequest;
import com.devspace.deployment.dto.DeploymentResponse;
import com.devspace.deployment.service.DeploymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects/{projectId}/deployments")
@RequiredArgsConstructor
public class DeploymentController {

    private final DeploymentService deploymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DeploymentResponse>> createDeployment(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateDeploymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Deployment created", deploymentService.createDeployment(projectId, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DeploymentResponse>>> getDeployments(
            @PathVariable Long projectId,
            Pageable pageable) {
        Page<DeploymentResponse> page = deploymentService.getDeployments(projectId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(page)));
    }

    @GetMapping("/{deploymentId}")
    public ResponseEntity<ApiResponse<DeploymentResponse>> getDeployment(
            @PathVariable Long projectId,
            @PathVariable Long deploymentId) {
        return ResponseEntity.ok(ApiResponse.success(deploymentService.getDeployment(projectId, deploymentId)));
    }

    @PostMapping("/{deploymentId}/rollback")
    public ResponseEntity<ApiResponse<DeploymentResponse>> rollback(
            @PathVariable Long projectId,
            @PathVariable Long deploymentId) {
        return ResponseEntity.ok(ApiResponse.success("Rollback initiated",
                deploymentService.rollback(projectId, deploymentId)));
    }
}
