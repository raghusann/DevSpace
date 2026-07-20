package com.devspace.database.controller;

import com.devspace.common.ApiResponse;
import com.devspace.database.dto.DatabaseConfigRequest;
import com.devspace.database.dto.SqlQueryRequest;
import com.devspace.database.dto.SqlQueryResponse;
import com.devspace.database.service.DatabaseExplorerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects/{projectId}/database")
@RequiredArgsConstructor
public class DatabaseExplorerController {

    private final DatabaseExplorerService databaseExplorerService;

    @PutMapping("/config")
    public ResponseEntity<ApiResponse<Void>> saveConfig(
            @PathVariable Long projectId,
            @Valid @RequestBody DatabaseConfigRequest request) {
        databaseExplorerService.saveConfig(projectId, request);
        return ResponseEntity.ok(ApiResponse.success("Database config saved", null));
    }

    @GetMapping("/tables")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getTables(@PathVariable Long projectId) {
        return ResponseEntity.ok(ApiResponse.success(databaseExplorerService.getTables(projectId)));
    }

    @PostMapping("/query")
    public ResponseEntity<ApiResponse<SqlQueryResponse>> executeQuery(
            @PathVariable Long projectId,
            @Valid @RequestBody SqlQueryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(databaseExplorerService.executeQuery(projectId, request)));
    }
}
