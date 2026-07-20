package com.devspace.github.entity;

import com.devspace.common.BaseEntity;
import com.devspace.project.entity.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "github_repos")
@Getter
@Setter
@NoArgsConstructor
public class GitHubRepo extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String repoFullName;

    @Column(nullable = false)
    private String repoUrl;

    private String defaultBranch;
}
