package com.devspace.github.service;

import com.devspace.auth.entity.User;
import com.devspace.auth.repository.UserRepository;
import com.devspace.common.SecurityUtils;
import com.devspace.common.exception.ResourceNotFoundException;
import com.devspace.github.dto.GitHubConnectionResponse;
import com.devspace.github.dto.GitHubRepoDto;
import com.devspace.github.dto.LinkRepoRequest;
import com.devspace.github.entity.GitHubConnection;
import com.devspace.github.entity.GitHubRepo;
import com.devspace.github.repository.GitHubConnectionRepository;
import com.devspace.github.repository.GitHubRepoRepository;
import com.devspace.project.entity.Project;
import com.devspace.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubConnectionRepository connectionRepository;
    private final GitHubRepoRepository repoRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public GitHubConnectionResponse getConnectionStatus() {
        User user = getCurrentUser();
        return connectionRepository.findByUserId(user.getId())
                .map(conn -> GitHubConnectionResponse.builder()
                        .connected(true)
                        .username(conn.getUsername())
                        .repositories(getLinkedRepos(user.getId()))
                        .build())
                .orElse(GitHubConnectionResponse.builder()
                        .connected(false)
                        .repositories(List.of())
                        .build());
    }

    @Transactional
    public GitHubConnectionResponse connect(String accessToken, String username) {
        User user = getCurrentUser();
        GitHubConnection connection = connectionRepository.findByUserId(user.getId())
                .orElse(new GitHubConnection());
        connection.setUserId(user.getId());
        connection.setAccessToken(accessToken);
        connection.setUsername(username);
        connectionRepository.save(connection);

        return GitHubConnectionResponse.builder()
                .connected(true)
                .username(username)
                .repositories(getLinkedRepos(user.getId()))
                .build();
    }

    @Transactional
    public GitHubRepoDto linkRepo(Long projectId, LinkRepoRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        GitHubRepo repo = new GitHubRepo();
        repo.setProject(project);
        repo.setRepoFullName(request.getRepoFullName());
        repo.setRepoUrl(request.getRepoUrl());
        repo.setDefaultBranch(request.getDefaultBranch());
        repo = repoRepository.save(repo);

        return mapRepo(repo);
    }

    @Transactional(readOnly = true)
    public List<GitHubRepoDto> getProjectRepos(Long projectId) {
        return repoRepository.findByProjectId(projectId).stream()
                .map(this::mapRepo)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMockBranches(String repoFullName) {
        return List.of(
                Map.of("name", "main", "commits", 142),
                Map.of("name", "develop", "commits", 89),
                Map.of("name", "feature/auth", "commits", 12)
        );
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMockPullRequests(String repoFullName) {
        return List.of(
                Map.of("number", 1, "title", "Add JWT authentication", "state", "open", "author", "dev1"),
                Map.of("number", 2, "title", "Fix kanban drag-drop", "state", "merged", "author", "dev2")
        );
    }

    private List<GitHubRepoDto> getLinkedRepos(Long userId) {
        return repoRepository.findAll().stream()
                .map(this::mapRepo)
                .toList();
    }

    private GitHubRepoDto mapRepo(GitHubRepo repo) {
        return GitHubRepoDto.builder()
                .id(repo.getId())
                .fullName(repo.getRepoFullName())
                .url(repo.getRepoUrl())
                .defaultBranch(repo.getDefaultBranch())
                .projectId(repo.getProject().getId())
                .build();
    }

    private User getCurrentUser() {
        return userRepository.findByEmail(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
