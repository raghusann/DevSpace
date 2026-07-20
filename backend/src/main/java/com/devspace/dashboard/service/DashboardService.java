package com.devspace.dashboard.service;

import com.devspace.activity.entity.Activity;
import com.devspace.activity.service.ActivityService;
import com.devspace.auth.entity.User;
import com.devspace.auth.repository.UserRepository;
import com.devspace.common.SecurityUtils;
import com.devspace.common.exception.ResourceNotFoundException;
import com.devspace.dashboard.dto.DashboardStatsResponse;
import com.devspace.kanban.repository.TaskRepository;
import com.devspace.notification.entity.Notification;
import com.devspace.notification.service.NotificationService;
import com.devspace.project.repository.ProjectMemberRepository;
import com.devspace.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;
    private final ActivityService activityService;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats() {
        User user = getCurrentUser();

        long projectCount = projectMemberRepository.findAll().stream()
                .filter(pm -> pm.getUser().getId().equals(user.getId()))
                .map(pm -> pm.getProject().getId())
                .distinct()
                .count();

        List<Activity> activities = activityService.getUserActivities(user.getId(), PageRequest.of(0, 10))
                .getContent();
        List<Notification> notifications = notificationService.getUserNotifications(user.getId(), PageRequest.of(0, 10))
                .getContent();

        long taskCount = 0;
        for (var pm : projectMemberRepository.findAll()) {
            if (pm.getUser().getId().equals(user.getId())) {
                taskCount += taskRepository.countByProjectId(pm.getProject().getId());
            }
        }

        return DashboardStatsResponse.builder()
                .projectCount(projectCount)
                .taskCount(taskCount)
                .unreadNotifications(notificationService.getUnreadCount(user.getId()))
                .recentActivities(activities)
                .recentNotifications(notifications)
                .build();
    }

    private User getCurrentUser() {
        return userRepository.findByEmail(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
