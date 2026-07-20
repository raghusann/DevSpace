package com.devspace.database.service;

import com.devspace.common.exception.BadRequestException;
import com.devspace.common.exception.ResourceNotFoundException;
import com.devspace.database.dto.DatabaseConfigRequest;
import com.devspace.database.dto.SqlQueryRequest;
import com.devspace.database.dto.SqlQueryResponse;
import com.devspace.database.entity.ProjectDatabaseConfig;
import com.devspace.database.repository.ProjectDatabaseConfigRepository;
import com.devspace.project.entity.Project;
import com.devspace.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatabaseExplorerService {

    private final ProjectDatabaseConfigRepository configRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void saveConfig(Long projectId, DatabaseConfigRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ProjectDatabaseConfig config = configRepository.findByProjectId(projectId)
                .orElse(new ProjectDatabaseConfig());
        config.setProject(project);
        config.setHost(request.getHost());
        config.setPort(request.getPort());
        config.setDatabaseName(request.getDatabaseName());
        config.setUsername(request.getUsername());
        config.setPassword(request.getPassword());
        configRepository.save(config);
    }

    @Transactional(readOnly = true)
    public SqlQueryResponse executeQuery(Long projectId, SqlQueryRequest request) {
        String query = request.getQuery().trim();
        if (!query.toUpperCase().startsWith("SELECT")) {
            throw new BadRequestException("Only SELECT queries are allowed for safety");
        }

        ProjectDatabaseConfig config = configRepository.findByProjectId(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Database not configured for this project"));

        String url = String.format("jdbc:postgresql://%s:%d/%s",
                config.getHost(), config.getPort(), config.getDatabaseName());

        try (Connection conn = DriverManager.getConnection(url, config.getUsername(), config.getPassword());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(meta.getColumnName(i));
            }

            List<Map<String, Object>> rows = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                rows.add(row);
            }

            return SqlQueryResponse.builder()
                    .columns(columns)
                    .rows(rows)
                    .rowCount(rows.size())
                    .message("Query executed successfully")
                    .build();
        } catch (Exception e) {
            throw new BadRequestException("Query failed: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Map<String, String>> getTables(Long projectId) {
        ProjectDatabaseConfig config = configRepository.findByProjectId(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Database not configured"));

        String url = String.format("jdbc:postgresql://%s:%d/%s",
                config.getHost(), config.getPort(), config.getDatabaseName());

        List<Map<String, String>> tables = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, config.getUsername(), config.getPassword());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name")) {
            while (rs.next()) {
                Map<String, String> table = new HashMap<>();
                table.put("name", rs.getString("table_name"));
                tables.add(table);
            }
        } catch (Exception e) {
            throw new BadRequestException("Failed to fetch tables: " + e.getMessage());
        }
        return tables;
    }
}
