package com.devspace.project.dto;

import com.devspace.common.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateMemberRoleRequest {

    @NotNull(message = "Role is required")
    private ProjectRole role;
}
