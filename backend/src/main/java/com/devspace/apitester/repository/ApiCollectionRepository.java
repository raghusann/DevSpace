package com.devspace.apitester.repository;

import com.devspace.apitester.entity.ApiCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiCollectionRepository extends JpaRepository<ApiCollection, Long> {

    List<ApiCollection> findByUserId(Long userId);
}
