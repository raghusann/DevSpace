package com.devspace.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class CreateProjectRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 200)
    private String name;

    private String description;

    @NotBlank(message = "Key is required")
    @Pattern(regexp = "^[A-Z][A-Z0-9]{1,9}$", message = "Key must be 2-10 uppercase alphanumeric characters starting with a letter")
    private String key;

    private Map<String, Object> settings;
}
