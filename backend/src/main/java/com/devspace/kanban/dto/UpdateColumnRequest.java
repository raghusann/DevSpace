package com.devspace.kanban.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateColumnRequest {

    @NotBlank
    private String name;

    private Integer position;

    private Integer wipLimit;
}
