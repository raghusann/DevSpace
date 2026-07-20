package com.devspace.project.dto;

import lombok.Data;

import java.util.Map;

@Data
public class UpdateProjectRequest {

    private String name;
    private String description;
    private String status;
    private Map<String, Object> settings;
}
