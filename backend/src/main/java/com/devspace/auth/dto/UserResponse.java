package com.devspace.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private boolean emailVerified;
    private Set<String> roles;
}
