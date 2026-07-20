package com.devspace.profile.controller;

import com.devspace.activity.entity.Activity;
import com.devspace.auth.dto.UserResponse;
import com.devspace.common.ApiResponse;
import com.devspace.common.PageResponse;
import com.devspace.profile.dto.ChangePasswordRequest;
import com.devspace.profile.dto.UpdateProfileRequest;
import com.devspace.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        return ResponseEntity.ok(ApiResponse.success(profileService.getProfile()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated", profileService.updateProfile(request)));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    @GetMapping("/activity")
    public ResponseEntity<ApiResponse<PageResponse<Activity>>> getActivity(Pageable pageable) {
        Page<Activity> page = profileService.getActivity(pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(page)));
    }
}
