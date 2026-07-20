package com.devspace.github.repository;

import com.devspace.github.entity.GitHubConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GitHubConnectionRepository extends JpaRepository<GitHubConnection, Long> {

    Optional<GitHubConnection> findByUserId(Long userId);
}
