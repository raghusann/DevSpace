package com.devspace.activity.repository;

import com.devspace.activity.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Page<Activity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<Activity> findByProjectIdOrderByCreatedAtDesc(Long projectId, Pageable pageable);
}
