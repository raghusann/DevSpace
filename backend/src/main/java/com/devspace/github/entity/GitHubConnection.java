package com.devspace.github.entity;

import com.devspace.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "github_connections")
@Getter
@Setter
@NoArgsConstructor
public class GitHubConnection extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String username;
}
