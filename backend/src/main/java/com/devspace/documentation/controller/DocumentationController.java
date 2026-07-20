package com.devspace.documentation.controller;

import com.devspace.common.ApiResponse;
import com.devspace.common.PageResponse;
import com.devspace.documentation.dto.CreateDocumentRequest;
import com.devspace.documentation.dto.DocumentResponse;
import com.devspace.documentation.service.DocumentationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects/{projectId}/documents")
@RequiredArgsConstructor
public class DocumentationController {

    private final DocumentationService documentationService;

    @PostMapping
    public ResponseEntity<ApiResponse<DocumentResponse>> createDocument(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateDocumentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Document created", documentationService.createDocument(projectId, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DocumentResponse>>> getDocuments(
            @PathVariable Long projectId,
            @RequestParam(required = false) String search,
            Pageable pageable) {
        Page<DocumentResponse> page = documentationService.getDocuments(projectId, search, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(page)));
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocument(
            @PathVariable Long projectId,
            @PathVariable Long documentId) {
        return ResponseEntity.ok(ApiResponse.success(documentationService.getDocument(projectId, documentId)));
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<ApiResponse<DocumentResponse>> updateDocument(
            @PathVariable Long projectId,
            @PathVariable Long documentId,
            @Valid @RequestBody CreateDocumentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Document updated",
                documentationService.updateDocument(projectId, documentId, request)));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(
            @PathVariable Long projectId,
            @PathVariable Long documentId) {
        documentationService.deleteDocument(projectId, documentId);
        return ResponseEntity.ok(ApiResponse.success("Document deleted", null));
    }
}
