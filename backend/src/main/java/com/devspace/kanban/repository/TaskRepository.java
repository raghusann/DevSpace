package com.devspace.kanban.repository;

import com.devspace.kanban.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByColumnIdOrderByPositionAsc(Long columnId);

    @Query("SELECT t FROM Task t JOIN BoardColumn c ON t.column = c JOIN Board b ON c.board = b WHERE b.project.id = :projectId")
    long countByProjectId(@Param("projectId") Long projectId);
}
