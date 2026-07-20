package com.devspace.docker.service;

import com.devspace.docker.dto.DockerContainerResponse;
import com.devspace.docker.dto.DockerImageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerService {

    public List<DockerContainerResponse> getContainers() {
        return List.of(
                DockerContainerResponse.builder()
                        .id("abc123")
                        .name("devspace-api")
                        .image("devspace/backend:latest")
                        .status("running")
                        .created("2 hours ago")
                        .ports(List.of("8080:8080"))
                        .build(),
                DockerContainerResponse.builder()
                        .id("def456")
                        .name("devspace-postgres")
                        .image("postgres:16-alpine")
                        .status("running")
                        .created("2 hours ago")
                        .ports(List.of("5432:5432"))
                        .build(),
                DockerContainerResponse.builder()
                        .id("ghi789")
                        .name("devspace-redis")
                        .image("redis:7-alpine")
                        .status("running")
                        .created("2 hours ago")
                        .ports(List.of("6379:6379"))
                        .build()
        );
    }

    public List<DockerImageResponse> getImages() {
        return List.of(
                DockerImageResponse.builder()
                        .id("img001")
                        .name("devspace/backend")
                        .tag("latest")
                        .size("245 MB")
                        .created("1 day ago")
                        .build(),
                DockerImageResponse.builder()
                        .id("img002")
                        .name("postgres")
                        .tag("16-alpine")
                        .size("238 MB")
                        .created("3 days ago")
                        .build()
        );
    }

    public String getContainerLogs(String containerId) {
        return "2026-07-14 10:00:01 INFO  DevSpaceApplication - Started DevSpaceApplication\n"
                + "2026-07-14 10:00:02 INFO  HikariPool-1 - Start completed\n"
                + "2026-07-14 10:00:03 INFO  Tomcat - Started on port 8080\n"
                + "Container: " + containerId;
    }

    public void stopContainer(String containerId) {
        // Docker API integration placeholder
    }

    public void restartContainer(String containerId) {
        // Docker API integration placeholder
    }

    public void deleteContainer(String containerId) {
        // Docker API integration placeholder
    }
}
