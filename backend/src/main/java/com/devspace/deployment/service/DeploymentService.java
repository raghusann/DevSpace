package com.devspace.deployment.service;

import com.devspace.common.enums.DeploymentStatus;
import com.devspace.common.exception.ResourceNotFoundException;
import com.devspace.deployment.dto.CreateDeploymentRequest;
import com.devspace.deployment.dto.DeploymentResponse;
import com.devspace.deployment.entity.Deployment;
import com.devspace.deployment.repository.DeploymentRepository;
import com.devspace.project.entity.Project;
import com.devspace.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeploymentService {

    private final DeploymentRepository deploymentRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public DeploymentResponse createDeployment(Long projectId, CreateDeploymentRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Deployment deployment = new Deployment();
        deployment.setProject(project);
        deployment.setEnvironment(request.getEnvironment());
        deployment.setVersion(request.getVersion());
        deployment.setEnvVariables(request.getEnvVariables());
        deployment.setStatus(DeploymentStatus.RUNNING);
        deployment.setLogs("Deployment started for " + request.getVersion() + " on " + request.getEnvironment());

        deployment = deploymentRepository.save(deployment);

        deployment.setStatus(DeploymentStatus.SUCCESS);
        deployment.setLogs(deployment.getLogs() + "\nDeployment completed successfully.");
        deployment = deploymentRepository.save(deployment);

        return mapResponse(deployment);
    }

    @Transactional(readOnly = true)
    public Page<DeploymentResponse> getDeployments(Long projectId, Pageable pageable) {
        return deploymentRepository.findByProjectIdOrderByCreatedAtDesc(projectId, pageable)
                .map(this::mapResponse);
    }

    @Transactional(readOnly = true)
    public DeploymentResponse getDeployment(Long projectId, Long deploymentId) {
        Deployment deployment = getDeploymentOrThrow(projectId, deploymentId);
        return mapResponse(deployment);
    }

    @Transactional
    public DeploymentResponse rollback(Long projectId, Long deploymentId) {
        Deployment deployment = getDeploymentOrThrow(projectId, deploymentId);
        deployment.setStatus(DeploymentStatus.ROLLED_BACK);
        deployment.setLogs(deployment.getLogs() + "\nRolled back to previous version.");
        return mapResponse(deploymentRepository.save(deployment));
    }

    private Deployment getDeploymentOrThrow(Long projectId, Long deploymentId) {
        Deployment deployment = deploymentRepository.findById(deploymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Deployment not found"));
        if (!deployment.getProject().getId().equals(projectId)) {
            throw new ResourceNotFoundException("Deployment not found in project");
        }
        return deployment;
    }

    private DeploymentResponse mapResponse(Deployment deployment) {
        return DeploymentResponse.builder()
                .id(deployment.getId())
                .projectId(deployment.getProject().getId())
                .environment(deployment.getEnvironment())
                .version(deployment.getVersion())
                .status(deployment.getStatus())
                .logs(deployment.getLogs())
                .envVariables(deployment.getEnvVariables())
                .createdAt(deployment.getCreatedAt())
                .updatedAt(deployment.getUpdatedAt())
                .build();
    }
}
