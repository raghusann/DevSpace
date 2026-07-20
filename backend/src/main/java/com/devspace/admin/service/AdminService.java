package com.devspace.admin.service;

import com.devspace.admin.dto.AdminAnalyticsResponse;
import com.devspace.admin.dto.AuditLogResponse;
import com.devspace.admin.entity.AuditLog;
import com.devspace.admin.repository.AuditLogRepository;
import com.devspace.auth.entity.User;
import com.devspace.auth.repository.UserRepository;
import com.devspace.kanban.repository.TaskRepository;
import com.devspace.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final AuditLogRepository auditLogRepository;

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void toggleUserStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        userRepository.save(user);
        logAction(userId, user.getEmail(), "TOGGLE_STATUS", "USER", userId,
                "User " + (enabled ? "enabled" : "disabled"));
    }

    @Transactional(readOnly = true)
    public AdminAnalyticsResponse getAnalytics() {
        return AdminAnalyticsResponse.builder()
                .totalUsers(userRepository.count())
                .totalProjects(projectRepository.count())
                .totalTasks(taskRepository.count())
                .activeUsers(userRepository.countByEnabledTrue())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getAuditLogs(Pageable pageable) {
        return auditLogRepository.findAllByOrderByCreatedAtDesc(pageable).map(this::mapAuditLog);
    }

    @Transactional
    public void logAction(Long userId, String email, String action, String entityType,
                          Long entityId, String details) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setUserEmail(email);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        auditLogRepository.save(log);
    }

    private AuditLogResponse mapAuditLog(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .userId(log.getUserId())
                .userEmail(log.getUserEmail())
                .action(log.getAction())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .details(log.getDetails())
                .ipAddress(log.getIpAddress())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
