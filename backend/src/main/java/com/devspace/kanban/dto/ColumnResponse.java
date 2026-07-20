package com.devspace.kanban.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ColumnResponse {

    private Long id;
    private String name;
    private int position;
    private Integer wipLimit;
    private List<TaskResponse> tasks;
}
