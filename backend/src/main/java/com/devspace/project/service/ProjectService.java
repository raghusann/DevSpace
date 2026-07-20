package com.devspace.project.service;

import com.devspace.activity.service.ActivityService;
import com.devspace.auth.entity.User;
import com.devspace.auth.repository.UserRepository;
import com.devspace.common.SecurityUtils;
import com.devspace.common.enums.ProjectRole;
import com.devspace.common.exception.BadRequestException;
import com.devspace.common.exception.ForbiddenException;
import com.devspace.common.exception.ResourceNotFoundException;
import com.devspace.notification.service.NotificationService;
import com.devspace.project.dto.CreateProjectRequest;
import com.devspace.project.dto.InviteMemberRequest;
import com.devspace.project.dto.ProjectMemberResponse;
import com.devspace.project.dto.ProjectResponse;
import com.devspace.project.dto.UpdateMemberRoleRequest;
import com.devspace.project.dto.UpdateProjectRequest;
import com.devspace.project.entity.Project;
import com.devspace.project.entity.ProjectMember;
import com.devspace.project.repository.ProjectMemberRepository;
import com.devspace.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;
    private final NotificationService notificationService;

    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {
        User owner = getCurrentUser();
        if (projectRepository.existsByKey(request.getKey())) {
            throw new BadRequestException("Project key already exists");
        }

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setKey(request.getKey().toUpperCase());
        project.setOwner(owner);
        project.setSettings(request.getSettings());
        project = projectRepository.save(project);

        ProjectMember ownerMember = new ProjectMember();
        ownerMember.setProject(project);
        ownerMember.setUser(owner);
        ownerMember.setRole(ProjectRole.OWNER);
        projectMemberRepository.save(ownerMember);

        activityService.logActivity(owner.getId(), project.getId(), "CREATED", "PROJECT", project.getId(),
                "Created project " + project.getName());

        return mapProjectResponse(project);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjects(Pageable pageable) {
        User user = getCurrentUser();
        return projectRepository.findByMemberUserId(user.getId(), pageable)
                .map(this::mapProjectResponse);
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long projectId) {
        Project project = getProjectOrThrow(projectId);
        verifyMemberAccess(project, getCurrentUser());
        return mapProjectResponse(project);
    }

    @Transactional
    public ProjectResponse updateProject(Long projectId, UpdateProjectRequest request) {
        Project project = getProjectOrThrow(projectId);
        verifyAdminAccess(project, getCurrentUser());

        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStatus() != null) project.setStatus(request.getStatus());
        if (request.getSettings() != null) project.setSettings(request.getSettings());

        project = projectRepository.save(project);
        return mapProjectResponse(project);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Project project = getProjectOrThrow(projectId);
        User user = getCurrentUser();
        if (!project.getOwner().getId().equals(user.getId())) {
            throw new ForbiddenException("Only the project owner can delete the project");
        }
        projectRepository.delete(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberResponse> getMembers(Long projectId) {
        Project project = getProjectOrThrow(projectId);
        verifyMemberAccess(project, getCurrentUser());
        return projectMemberRepository.findByProjectId(projectId).stream()
                .map(this::mapMemberResponse)
                .toList();
    }

    @Transactional
    public ProjectMemberResponse inviteMember(Long projectId, InviteMemberRequest request) {
        Project project = getProjectOrThrow(projectId);
        User inviter = getCurrentUser();
        verifyAdminAccess(project, inviter);

        User invitee = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        if (projectMemberRepository.existsByProjectIdAndUserId(projectId, invitee.getId())) {
            throw new BadRequestException("User is already a member");
        }

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(invitee);
        member.setRole(request.getRole());
        member = projectMemberRepository.save(member);

        notificationService.createNotification(invitee.getId(), "Project Invitation",
                "You were added to project " + project.getName(), "PROJECT_INVITE", projectId);

        return mapMemberResponse(member);
    }

    @Transactional
    public void removeMember(Long projectId, Long userId) {
        Project project = getProjectOrThrow(projectId);
        verifyAdminAccess(project, getCurrentUser());

        if (project.getOwner().getId().equals(userId)) {
            throw new BadRequestException("Cannot remove project owner");
        }

        projectMemberRepository.deleteByProjectIdAndUserId(projectId, userId);
    }

    @Transactional
    public ProjectMemberResponse updateMemberRole(Long projectId, Long userId, UpdateMemberRoleRequest request) {
        Project project = getProjectOrThrow(projectId);
        verifyAdminAccess(project, getCurrentUser());

        if (project.getOwner().getId().equals(userId)) {
            throw new BadRequestException("Cannot change owner role");
        }

        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        member.setRole(request.getRole());
        member = projectMemberRepository.save(member);
        return mapMemberResponse(member);
    }

    public Project getProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    public void verifyMemberAccess(Project project, User user) {
        if (!projectMemberRepository.existsByProjectIdAndUserId(project.getId(), user.getId())) {
            throw new ForbiddenException("You are not a member of this project");
        }
    }

    public void verifyAdminAccess(Project project, User user) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(project.getId(), user.getId())
                .orElseThrow(() -> new ForbiddenException("You are not a member of this project"));
        if (member.getRole() != ProjectRole.OWNER && member.getRole() != ProjectRole.ADMIN) {
            throw new ForbiddenException("Admin access required");
        }
    }

    private User getCurrentUser() {
        return userRepository.findByEmail(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private ProjectResponse mapProjectResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .key(project.getKey())
                .ownerId(project.getOwner().getId())
                .ownerName(project.getOwner().getFirstName() + " " + project.getOwner().getLastName())
                .status(project.getStatus())
                .settings(project.getSettings())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    private ProjectMemberResponse mapMemberResponse(ProjectMember member) {
        User user = member.getUser();
        return ProjectMemberResponse.builder()
                .id(member.getId())
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatar(user.getAvatar())
                .role(member.getRole())
                .joinedAt(member.getCreatedAt())
                .build();
    }
}
