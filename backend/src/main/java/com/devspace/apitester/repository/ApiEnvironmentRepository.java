package com.devspace.apitester.repository;

import com.devspace.apitester.entity.ApiEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiEnvironmentRepository extends JpaRepository<ApiEnvironment, Long> {

    List<ApiEnvironment> findByUserId(Long userId);
}
