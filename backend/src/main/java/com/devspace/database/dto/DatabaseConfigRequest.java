package com.devspace.database.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DatabaseConfigRequest {

    @NotBlank(message = "Host is required")
    private String host;

    private int port = 5432;

    @NotBlank(message = "Database name is required")
    private String databaseName;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
