package com.devspace.project.dto;

import com.devspace.common.enums.ProjectRole;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ProjectMemberResponse {

    private Long id;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private ProjectRole role;
    private Instant joinedAt;
}
