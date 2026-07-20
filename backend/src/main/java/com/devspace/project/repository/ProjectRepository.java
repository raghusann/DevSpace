package com.devspace.project.repository;

import com.devspace.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByKey(String key);

    boolean existsByKey(String key);

    @Query("SELECT p FROM Project p JOIN ProjectMember pm ON pm.project = p WHERE pm.user.id = :userId")
    Page<Project> findByMemberUserId(@Param("userId") Long userId, Pageable pageable);

    long countByOwnerId(Long ownerId);
}
