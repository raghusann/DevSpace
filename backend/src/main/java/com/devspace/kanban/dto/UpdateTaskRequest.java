package com.devspace.kanban.dto;

import com.devspace.common.enums.TaskPriority;
import com.devspace.common.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateTaskRequest {

    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long assigneeId;
    private LocalDate dueDate;
    private Integer position;
}
