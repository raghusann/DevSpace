package com.devspace.database.repository;

import com.devspace.database.entity.ProjectDatabaseConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectDatabaseConfigRepository extends JpaRepository<ProjectDatabaseConfig, Long> {

    Optional<ProjectDatabaseConfig> findByProjectId(Long projectId);
}
