package com.devspace.dashboard.dto;

import com.devspace.activity.entity.Activity;
import com.devspace.notification.entity.Notification;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardStatsResponse {

    private long projectCount;
    private long taskCount;
    private long unreadNotifications;
    private List<Activity> recentActivities;
    private List<Notification> recentNotifications;
}
