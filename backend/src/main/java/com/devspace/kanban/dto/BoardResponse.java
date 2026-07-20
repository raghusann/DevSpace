package com.devspace.kanban.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardResponse {

    private Long id;
    private String name;
    private String description;
    private List<ColumnResponse> columns;
}
