package com.devspace.documentation.service;

import com.devspace.common.exception.ResourceNotFoundException;
import com.devspace.documentation.dto.CreateDocumentRequest;
import com.devspace.documentation.dto.DocumentResponse;
import com.devspace.documentation.entity.ProjectDocument;
import com.devspace.documentation.repository.ProjectDocumentRepository;
import com.devspace.project.entity.Project;
import com.devspace.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DocumentationService {

    private final ProjectDocumentRepository documentRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public DocumentResponse createDocument(Long projectId, CreateDocumentRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ProjectDocument doc = new ProjectDocument();
        doc.setProject(project);
        doc.setTitle(request.getTitle());
        doc.setContent(request.getContent());
        doc = documentRepository.save(doc);

        return mapResponse(doc);
    }

    @Transactional(readOnly = true)
    public Page<DocumentResponse> getDocuments(Long projectId, String search, Pageable pageable) {
        Page<ProjectDocument> page;
        if (search != null && !search.isBlank()) {
            page = documentRepository.findByProjectIdAndTitleContainingIgnoreCase(projectId, search, pageable);
        } else {
            page = documentRepository.findByProjectId(projectId, pageable);
        }
        return page.map(this::mapResponse);
    }

    @Transactional(readOnly = true)
    public DocumentResponse getDocument(Long projectId, Long documentId) {
        return mapResponse(getDocumentOrThrow(projectId, documentId));
    }

    @Transactional
    public DocumentResponse updateDocument(Long projectId, Long documentId, CreateDocumentRequest request) {
        ProjectDocument doc = getDocumentOrThrow(projectId, documentId);
        doc.setTitle(request.getTitle());
        doc.setContent(request.getContent());
        return mapResponse(documentRepository.save(doc));
    }

    @Transactional
    public void deleteDocument(Long projectId, Long documentId) {
        documentRepository.delete(getDocumentOrThrow(projectId, documentId));
    }

    private ProjectDocument getDocumentOrThrow(Long projectId, Long documentId) {
        ProjectDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
        if (!doc.getProject().getId().equals(projectId)) {
            throw new ResourceNotFoundException("Document not found in project");
        }
        return doc;
    }

    private DocumentResponse mapResponse(ProjectDocument doc) {
        return DocumentResponse.builder()
                .id(doc.getId())
                .projectId(doc.getProject().getId())
                .title(doc.getTitle())
                .content(doc.getContent())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }
}
