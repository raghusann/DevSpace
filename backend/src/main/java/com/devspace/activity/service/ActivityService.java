package com.devspace.activity.service;

import com.devspace.activity.entity.Activity;
import com.devspace.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    @Transactional
    public void logActivity(Long userId, Long projectId, String action, String entityType, Long entityId, String details) {
        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setProjectId(projectId);
        activity.setAction(action);
        activity.setEntityType(entityType);
        activity.setEntityId(entityId);
        activity.setDetails(details);
        activityRepository.save(activity);
    }

    @Transactional(readOnly = true)
    public Page<Activity> getUserActivities(Long userId, Pageable pageable) {
        return activityRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
}
