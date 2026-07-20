package com.devspace.documentation.repository;

import com.devspace.documentation.entity.ProjectDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDocumentRepository extends JpaRepository<ProjectDocument, Long> {

    Page<ProjectDocument> findByProjectId(Long projectId, Pageable pageable);

    Page<ProjectDocument> findByProjectIdAndTitleContainingIgnoreCase(Long projectId, String title, Pageable pageable);
}
