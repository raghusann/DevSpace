package com.devspace.deployment.repository;

import com.devspace.deployment.entity.Deployment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {

    Page<Deployment> findByProjectIdOrderByCreatedAtDesc(Long projectId, Pageable pageable);

    List<Deployment> findByProjectIdAndEnvironmentOrderByCreatedAtDesc(Long projectId, String environment);
}
