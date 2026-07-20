package com.devspace.github.repository;

import com.devspace.github.entity.GitHubRepo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GitHubRepoRepository extends JpaRepository<GitHubRepo, Long> {

    List<GitHubRepo> findByProjectId(Long projectId);

    Optional<GitHubRepo> findByProjectIdAndRepoFullName(Long projectId, String repoFullName);
}
