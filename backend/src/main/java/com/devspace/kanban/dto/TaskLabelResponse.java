package com.devspace.kanban.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskLabelResponse {

    private Long id;
    private String name;
    private String color;
}
