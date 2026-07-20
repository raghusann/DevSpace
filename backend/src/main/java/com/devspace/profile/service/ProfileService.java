package com.devspace.profile.service;

import com.devspace.activity.entity.Activity;
import com.devspace.activity.service.ActivityService;
import com.devspace.auth.dto.UserResponse;
import com.devspace.auth.entity.User;
import com.devspace.auth.repository.UserRepository;
import com.devspace.common.SecurityUtils;
import com.devspace.common.exception.BadRequestException;
import com.devspace.common.exception.ResourceNotFoundException;
import com.devspace.profile.dto.ChangePasswordRequest;
import com.devspace.profile.dto.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivityService activityService;

    @Transactional(readOnly = true)
    public UserResponse getProfile() {
        User user = getCurrentUser();
        return mapUserResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        user = userRepository.save(user);
        return mapUserResponse(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<Activity> getActivity(Pageable pageable) {
        User user = getCurrentUser();
        return activityService.getUserActivities(user.getId(), pageable);
    }

    private User getCurrentUser() {
        String email = SecurityUtils.getCurrentUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserResponse mapUserResponse(User user) {
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
