package com.devspace.github.controller;

import com.devspace.common.ApiResponse;
import com.devspace.github.dto.GitHubConnectionResponse;
import com.devspace.github.dto.GitHubRepoDto;
import com.devspace.github.dto.LinkRepoRequest;
import com.devspace.github.service.GitHubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<GitHubConnectionResponse>> getStatus() {
        return ResponseEntity.ok(ApiResponse.success(gitHubService.getConnectionStatus()));
    }

    @PostMapping("/connect")
    public ResponseEntity<ApiResponse<GitHubConnectionResponse>> connect(
            @RequestParam String accessToken,
            @RequestParam String username) {
        return ResponseEntity.ok(ApiResponse.success("GitHub connected",
                gitHubService.connect(accessToken, username)));
    }

    @PostMapping("/projects/{projectId}/repos")
    public ResponseEntity<ApiResponse<GitHubRepoDto>> linkRepo(
            @PathVariable Long projectId,
            @Valid @RequestBody LinkRepoRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Repository linked",
                gitHubService.linkRepo(projectId, request)));
    }

    @GetMapping("/projects/{projectId}/repos")
    public ResponseEntity<ApiResponse<List<GitHubRepoDto>>> getProjectRepos(@PathVariable Long projectId) {
        return ResponseEntity.ok(ApiResponse.success(gitHubService.getProjectRepos(projectId)));
    }

    @GetMapping("/repos/{repoFullName}/branches")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getBranches(
            @PathVariable String repoFullName) {
        return ResponseEntity.ok(ApiResponse.success(gitHubService.getMockBranches(repoFullName)));
    }

    @GetMapping("/repos/{repoFullName}/pull-requests")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPullRequests(
            @PathVariable String repoFullName) {
        return ResponseEntity.ok(ApiResponse.success(gitHubService.getMockPullRequests(repoFullName)));
    }
}
