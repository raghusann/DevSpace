package com.devspace.kanban.repository;

import com.devspace.kanban.entity.TaskLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskLabelRepository extends JpaRepository<TaskLabel, Long> {

    List<TaskLabel> findByTaskId(Long taskId);
}
