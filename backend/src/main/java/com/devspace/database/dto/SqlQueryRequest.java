package com.devspace.database.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SqlQueryRequest {

    @NotBlank(message = "SQL query is required")
    private String query;
}
