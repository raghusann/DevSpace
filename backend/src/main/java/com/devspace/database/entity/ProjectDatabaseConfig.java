package com.devspace.database.entity;

import com.devspace.common.BaseEntity;
import com.devspace.project.entity.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project_database_configs")
@Getter
@Setter
@NoArgsConstructor
public class ProjectDatabaseConfig extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private Project project;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private int port = 5432;

    @Column(nullable = false)
    private String databaseName;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
}
