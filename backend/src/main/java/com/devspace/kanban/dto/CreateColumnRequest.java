package com.devspace.kanban.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateColumnRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private int position;
    private Integer wipLimit;
}
