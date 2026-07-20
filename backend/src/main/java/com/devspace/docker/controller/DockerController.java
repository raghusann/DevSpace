package com.devspace.docker.controller;

import com.devspace.common.ApiResponse;
import com.devspace.docker.dto.DockerContainerResponse;
import com.devspace.docker.dto.DockerImageResponse;
import com.devspace.docker.service.DockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/docker")
@RequiredArgsConstructor
public class DockerController {

    private final DockerService dockerService;

    @GetMapping("/containers")
    public ResponseEntity<ApiResponse<List<DockerContainerResponse>>> getContainers() {
        return ResponseEntity.ok(ApiResponse.success(dockerService.getContainers()));
    }

    @GetMapping("/images")
    public ResponseEntity<ApiResponse<List<DockerImageResponse>>> getImages() {
        return ResponseEntity.ok(ApiResponse.success(dockerService.getImages()));
    }

    @GetMapping("/containers/{containerId}/logs")
    public ResponseEntity<ApiResponse<String>> getLogs(@PathVariable String containerId) {
        return ResponseEntity.ok(ApiResponse.success(dockerService.getContainerLogs(containerId)));
    }

    @PostMapping("/containers/{containerId}/stop")
    public ResponseEntity<ApiResponse<Void>> stopContainer(@PathVariable String containerId) {
        dockerService.stopContainer(containerId);
        return ResponseEntity.ok(ApiResponse.success("Container stopped", null));
    }

    @PostMapping("/containers/{containerId}/restart")
    public ResponseEntity<ApiResponse<Void>> restartContainer(@PathVariable String containerId) {
        dockerService.restartContainer(containerId);
        return ResponseEntity.ok(ApiResponse.success("Container restarted", null));
    }

    @DeleteMapping("/containers/{containerId}")
    public ResponseEntity<ApiResponse<Void>> deleteContainer(@PathVariable String containerId) {
        dockerService.deleteContainer(containerId);
        return ResponseEntity.ok(ApiResponse.success("Container deleted", null));
    }
}
