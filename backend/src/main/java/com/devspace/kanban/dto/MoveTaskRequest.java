package com.devspace.kanban.dto;

import lombok.Data;

@Data
public class MoveTaskRequest {

    private Long targetColumnId;
    private int position;
}
