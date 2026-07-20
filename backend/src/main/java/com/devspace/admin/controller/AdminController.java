package com.devspace.admin.controller;

import com.devspace.admin.dto.AdminAnalyticsResponse;
import com.devspace.admin.dto.AuditLogResponse;
import com.devspace.admin.service.AdminService;
import com.devspace.auth.dto.UserResponse;
import com.devspace.auth.entity.User;
import com.devspace.common.ApiResponse;
import com.devspace.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUsers(Pageable pageable) {
        Page<User> page = adminService.getAllUsers(pageable);
        Page<UserResponse> mapped = page.map(this::mapUser);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(mapped)));
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<Void>> toggleUserStatus(
            @PathVariable Long userId,
            @RequestParam boolean enabled) {
        adminService.toggleUserStatus(userId, enabled);
        return ResponseEntity.ok(ApiResponse.success("User status updated", null));
    }

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<AdminAnalyticsResponse>> getAnalytics() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getAnalytics()));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<PageResponse<AuditLogResponse>>> getAuditLogs(Pageable pageable) {
        Page<AuditLogResponse> page = adminService.getAuditLogs(pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(page)));
    }

    private UserResponse mapUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatar(user.getAvatar())
                .emailVerified(user.isEmailVerified())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()))
                .build();
    }
}
