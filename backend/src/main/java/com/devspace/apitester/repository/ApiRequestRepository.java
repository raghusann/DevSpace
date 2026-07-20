package com.devspace.apitester.repository;

import com.devspace.apitester.entity.ApiRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiRequestRepository extends JpaRepository<ApiRequest, Long> {

    List<ApiRequest> findByCollectionId(Long collectionId);
}
