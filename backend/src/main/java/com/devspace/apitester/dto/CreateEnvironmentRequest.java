package com.devspace.apitester.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class CreateEnvironmentRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private Map<String, String> variables;
}
