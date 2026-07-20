package com.devspace.kanban.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {

    private Long id;
    private String content;
    private Long authorId;
    private String authorName;
    private Instant createdAt;
}
