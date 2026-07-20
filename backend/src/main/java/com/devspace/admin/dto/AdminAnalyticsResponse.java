package com.devspace.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminAnalyticsResponse {

    private long totalUsers;
    private long totalProjects;
    private long totalTasks;
    private long activeUsers;
}
