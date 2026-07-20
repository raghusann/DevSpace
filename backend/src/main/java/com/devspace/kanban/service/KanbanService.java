package com.devspace.kanban.service;

import com.devspace.auth.entity.User;
import com.devspace.auth.repository.UserRepository;
import com.devspace.common.SecurityUtils;
import com.devspace.common.enums.TaskPriority;
import com.devspace.common.enums.TaskStatus;
import com.devspace.common.exception.ResourceNotFoundException;
import com.devspace.kanban.dto.*;
import com.devspace.kanban.entity.Board;
import com.devspace.kanban.entity.BoardColumn;
import com.devspace.kanban.entity.Task;
import com.devspace.kanban.entity.TaskComment;
import com.devspace.kanban.repository.BoardColumnRepository;
import com.devspace.kanban.repository.BoardRepository;
import com.devspace.kanban.repository.TaskCommentRepository;
import com.devspace.kanban.repository.TaskLabelRepository;
import com.devspace.kanban.repository.TaskRepository;
import com.devspace.project.entity.Project;
import com.devspace.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KanbanService {

    private final BoardRepository boardRepository;
    private final BoardColumnRepository columnRepository;
    private final TaskRepository taskRepository;
    private final TaskCommentRepository commentRepository;
    private final TaskLabelRepository labelRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;

    @Transactional
    public BoardResponse createBoard(Long projectId, CreateBoardRequest request) {
        Project project = verifyProjectAccess(projectId);
        Board board = new Board();
        board.setProject(project);
        board.setName(request.getName());
        board.setDescription(request.getDescription());
        board = boardRepository.save(board);

        createDefaultColumns(board);
        return getBoard(projectId, board.getId());
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getBoards(Long projectId) {
        verifyProjectAccess(projectId);
        return boardRepository.findByProjectId(projectId).stream()
                .map(board -> getBoard(projectId, board.getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public BoardResponse getBoard(Long projectId, Long boardId) {
        verifyProjectAccess(projectId);
        Board board = boardRepository.findByIdAndProjectId(boardId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));

        List<ColumnResponse> columns = columnRepository.findByBoardIdOrderByPositionAsc(boardId).stream()
                .map(col -> ColumnResponse.builder()
                        .id(col.getId())
                        .name(col.getName())
                        .position(col.getPosition())
                        .wipLimit(col.getWipLimit())
                        .tasks(taskRepository.findByColumnIdOrderByPositionAsc(col.getId()).stream()
                                .map(this::mapTask)
                                .toList())
                        .build())
                .toList();

        return BoardResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .description(board.getDescription())
                .columns(columns)
                .build();
    }

    @Transactional
    public ColumnResponse createColumn(Long projectId, Long boardId, CreateColumnRequest request) {
        verifyProjectAccess(projectId);
        Board board = boardRepository.findByIdAndProjectId(boardId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));

        BoardColumn column = new BoardColumn();
        column.setBoard(board);
        column.setName(request.getName());
        column.setPosition(request.getPosition());
        column.setWipLimit(request.getWipLimit());
        column = columnRepository.save(column);

        return ColumnResponse.builder()
                .id(column.getId())
                .name(column.getName())
                .position(column.getPosition())
                .wipLimit(column.getWipLimit())
                .tasks(List.of())
                .build();
    }
    @Transactional
    public ColumnResponse updateColumn(Long projectId,
                                       Long boardId,
                                       Long columnId,
                                       @Valid UpdateColumnRequest request) {

        verifyProjectAccess(projectId);

        BoardColumn column = getColumnForBoard(projectId, boardId, columnId);

        if (request.getName() != null)
            column.setName(request.getName());

        if (request.getPosition() != null)
            column.setPosition(request.getPosition());

        if (request.getWipLimit() != null)
            column.setWipLimit(request.getWipLimit());

        column = columnRepository.save(column);

        return ColumnResponse.builder()
                .id(column.getId())
                .name(column.getName())
                .position(column.getPosition())
                .wipLimit(column.getWipLimit())
                .tasks(
                        taskRepository.findByColumnIdOrderByPositionAsc(column.getId())
                                .stream()
                                .map(this::mapTask)
                                .toList()
                )
                .build();
    }

    @Transactional
    public void deleteColumn(Long projectId,
                             Long boardId,
                             Long columnId) {

        verifyProjectAccess(projectId);

        BoardColumn column = getColumnForBoard(projectId, boardId, columnId);

        if (!taskRepository.findByColumnIdOrderByPositionAsc(columnId).isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete a column that contains tasks."
            );
        }

        columnRepository.delete(column);
    }

    @Transactional
    public TaskResponse createTask(Long projectId, Long boardId, Long columnId, CreateTaskRequest request) {
        verifyProjectAccess(projectId);
        BoardColumn column = getColumnForBoard(projectId, boardId, columnId);
        User reporter = getCurrentUser();

        Task task = new Task();
        task.setColumn(column);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO);
        task.setPriority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM);
        task.setPosition(request.getPosition());
        task.setReporter(reporter);
        task.setDueDate(request.getDueDate());

        if (request.getAssigneeId() != null) {
            task.setAssignee(userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found")));
        }

        task = taskRepository.save(task);
        return mapTask(task);
    }

    @Transactional
    public TaskResponse updateTask(Long projectId, Long boardId, Long taskId, UpdateTaskRequest request) {
        verifyProjectAccess(projectId);
        Task task = getTaskForBoard(projectId, boardId, taskId);

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getPosition() != null) task.setPosition(request.getPosition());
        if (request.getAssigneeId() != null) {
            task.setAssignee(userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found")));
        }

        task = taskRepository.save(task);
        return mapTask(task);
    }

    @Transactional
    public TaskResponse moveTask(Long projectId, Long boardId, Long taskId, MoveTaskRequest request) {
        verifyProjectAccess(projectId);
        Task task = getTaskForBoard(projectId, boardId, taskId);
        BoardColumn targetColumn = getColumnForBoard(projectId, boardId, request.getTargetColumnId());
        task.setColumn(targetColumn);
        task.setPosition(request.getPosition());
        task = taskRepository.save(task);
        return mapTask(task);
    }

    @Transactional
    public void deleteTask(Long projectId, Long boardId, Long taskId) {
        verifyProjectAccess(projectId);
        Task task = getTaskForBoard(projectId, boardId, taskId);
        taskRepository.delete(task);
    }

    @Transactional
    public CommentResponse addComment(Long projectId, Long boardId, Long taskId, CreateCommentRequest request) {
        verifyProjectAccess(projectId);
        Task task = getTaskForBoard(projectId, boardId, taskId);
        User author = getCurrentUser();

        TaskComment comment = new TaskComment();
        comment.setTask(task);
        comment.setAuthor(author);
        comment.setContent(request.getContent());
        comment = commentRepository.save(comment);

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorId(author.getId())
                .authorName(author.getFirstName() + " " + author.getLastName())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long projectId, Long boardId, Long taskId) {
        verifyProjectAccess(projectId);
        getTaskForBoard(projectId, boardId, taskId);
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId).stream()
                .map(c -> CommentResponse.builder()
                        .id(c.getId())
                        .content(c.getContent())
                        .authorId(c.getAuthor().getId())
                        .authorName(c.getAuthor().getFirstName() + " " + c.getAuthor().getLastName())
                        .createdAt(c.getCreatedAt())
                        .build())
                .toList();
    }

    private void createDefaultColumns(Board board) {
        String[] defaults = {"To Do", "In Progress", "Testing", "Done"};
        for (int i = 0; i < defaults.length; i++) {
            BoardColumn column = new BoardColumn();
            column.setBoard(board);
            column.setName(defaults[i]);
            column.setPosition(i);
            columnRepository.save(column);
        }
    }

    private Project verifyProjectAccess(Long projectId) {
        Project project = projectService.getProjectOrThrow(projectId);
        projectService.verifyMemberAccess(project, getCurrentUser());
        return project;
    }

    private BoardColumn getColumnForBoard(Long projectId, Long boardId, Long columnId) {
        boardRepository.findByIdAndProjectId(boardId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        return columnRepository.findById(columnId)
                .filter(c -> c.getBoard().getId().equals(boardId))
                .orElseThrow(() -> new ResourceNotFoundException("Column not found"));
    }

    private Task getTaskForBoard(Long projectId, Long boardId, Long taskId) {
        boardRepository.findByIdAndProjectId(boardId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        return taskRepository.findById(taskId)
                .filter(t -> t.getColumn().getBoard().getId().equals(boardId))
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    private User getCurrentUser() {
        return userRepository.findByEmail(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private TaskResponse mapTask(Task task) {
        String assigneeName = task.getAssignee() != null
                ? task.getAssignee().getFirstName() + " " + task.getAssignee().getLastName()
                : null;

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .position(task.getPosition())
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .assigneeName(assigneeName)
                .reporterId(task.getReporter() != null ? task.getReporter().getId() : null)
                .dueDate(task.getDueDate())
                .labels(labelRepository.findByTaskId(task.getId()).stream()
                        .map(l -> TaskLabelResponse.builder()
                                .id(l.getId())
                                .name(l.getName())
                                .color(l.getColor())
                                .build())
                        .toList())
                .build();
    }
}
