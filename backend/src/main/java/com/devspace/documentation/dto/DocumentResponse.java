package com.devspace.documentation.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class DocumentResponse {

    private Long id;
    private Long projectId;
    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
}
