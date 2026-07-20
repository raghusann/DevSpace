package com.devspace.kanban.controller;

import com.devspace.kanban.dto.UpdateColumnRequest;
import com.devspace.common.ApiResponse;
import com.devspace.kanban.dto.BoardResponse;
import com.devspace.kanban.dto.ColumnResponse;
import com.devspace.kanban.dto.CommentResponse;
import com.devspace.kanban.dto.CreateBoardRequest;
import com.devspace.kanban.dto.CreateColumnRequest;
import com.devspace.kanban.dto.CreateCommentRequest;
import com.devspace.kanban.dto.CreateTaskRequest;
import com.devspace.kanban.dto.MoveTaskRequest;
import com.devspace.kanban.dto.TaskResponse;
import com.devspace.kanban.dto.UpdateTaskRequest;
import com.devspace.kanban.service.KanbanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/kanban")
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanService kanbanService;

    @PostMapping("/boards")
    public ResponseEntity<ApiResponse<BoardResponse>> createBoard(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateBoardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Board created", kanbanService.createBoard(projectId, request)));
    }
    @PutMapping("/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<ApiResponse<ColumnResponse>> updateColumn(
            @PathVariable Long projectId,
            @PathVariable Long boardId,
            @PathVariable Long columnId,
            @Valid @RequestBody UpdateColumnRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Column updated",
                        kanbanService.updateColumn(projectId, boardId, columnId, request)
                )
        );
    }

    @DeleteMapping("/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<ApiResponse<Void>> deleteColumn(
            @PathVariable Long projectId,
            @PathVariable Long boardId,
            @PathVariable Long columnId) {

        kanbanService.deleteColumn(projectId, boardId, columnId);

        return ResponseEntity.ok(
                ApiResponse.success("Column deleted", null)
        );
    }

    @GetMapping("/boards")
    public ResponseEntity<ApiResponse<List<BoardResponse>>> getBoards(@PathVariable Long projectId) {
        return ResponseEntity.ok(ApiResponse.success(kanbanService.getBoards(projectId)));
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoard(
            @PathVariable Long projectId,
            @PathVariable Long boardId) {
        return ResponseEntity.ok(ApiResponse.success(kanbanService.getBoard(projectId, boardId)));
    }

    @PostMapping("/boards/{boardId}/columns")
    public ResponseEntity<ApiResponse<ColumnResponse>> createColumn(
            @PathVariable Long projectId,
            @PathVariable Long boardId,
            @Valid @RequestBody CreateColumnRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Column created", kanbanService.createColumn(projectId, boardId, request)));
    }

    @PostMapping("/boards/{boardId}/columns/{columnId}/tasks")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @PathVariable Long projectId,
            @PathVariable Long boardId,
            @PathVariable Long columnId,
            @Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task created",
                        kanbanService.createTask(projectId, boardId, columnId, request)));
    }

    @PutMapping("/boards/{boardId}/tasks/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long projectId,
            @PathVariable Long boardId,
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Task updated",
                kanbanService.updateTask(projectId, boardId, taskId, request)));
    }

    @PutMapping("/boards/{boardId}/tasks/{taskId}/move")
    public ResponseEntity<ApiResponse<TaskResponse>> moveTask(
            @PathVariable Long projectId,
            @PathVariable Long boardId,
            @PathVariable Long taskId,
            @Valid @RequestBody MoveTaskRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Task moved",
                kanbanService.moveTask(projectId, boardId, taskId, request)));
    }

    @DeleteMapping("/boards/{boardId}/tasks/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long boardId,
            @PathVariable Long taskId) {
        kanbanService.deleteTask(projectId, boardId, taskId);
        return ResponseEntity.ok(ApiResponse.success("Task deleted", null));
    }

    @PostMapping("/boards/{boardId}/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable Long projectId,
            @PathVariable Long boardId,
            @PathVariable Long taskId,
            @Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment added",
                        kanbanService.addComment(projectId, boardId, taskId, request)));
    }

    @GetMapping("/boards/{boardId}/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable Long projectId,
            @PathVariable Long boardId,
            @PathVariable Long taskId) {
        return ResponseEntity.ok(ApiResponse.success(kanbanService.getComments(projectId, boardId, taskId)));
    }
}
