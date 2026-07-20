package com.devspace.apitester.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class CreateApiRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Method is required")
    private String method;

    @NotBlank(message = "URL is required")
    private String url;

    private Map<String, String> headers;
    private String body;
}
