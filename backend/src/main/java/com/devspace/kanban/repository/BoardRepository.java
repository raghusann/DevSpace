package com.devspace.kanban.repository;

import com.devspace.kanban.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByProjectId(Long projectId);

    Optional<Board> findByIdAndProjectId(Long id, Long projectId);
}
