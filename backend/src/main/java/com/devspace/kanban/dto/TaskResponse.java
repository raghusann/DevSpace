package com.devspace.kanban.dto;

import com.devspace.common.enums.TaskPriority;
import com.devspace.common.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private int position;
    private Long assigneeId;
    private String assigneeName;
    private Long reporterId;
    private LocalDate dueDate;
    private List<TaskLabelResponse> labels;
}
