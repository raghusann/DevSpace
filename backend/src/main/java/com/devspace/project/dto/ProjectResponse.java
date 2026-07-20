package com.devspace.project.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private String key;
    private Long ownerId;
    private String ownerName;
    private String status;
    private Map<String, Object> settings;
    private Instant createdAt;
    private Instant updatedAt;
}
