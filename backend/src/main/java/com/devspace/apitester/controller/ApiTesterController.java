package com.devspace.apitester.controller;

import com.devspace.apitester.dto.CreateApiRequestDto;
import com.devspace.apitester.dto.CreateCollectionRequest;
import com.devspace.apitester.dto.CreateEnvironmentRequest;
import com.devspace.apitester.dto.ProxyRequest;
import com.devspace.apitester.dto.ProxyResponse;
import com.devspace.apitester.entity.ApiCollection;
import com.devspace.apitester.entity.ApiEnvironment;
import com.devspace.apitester.entity.ApiRequest;
import com.devspace.apitester.service.ApiTesterService;
import com.devspace.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/apitester")
@RequiredArgsConstructor
public class ApiTesterController {

    private final ApiTesterService apiTesterService;

    @PostMapping("/collections")
    public ResponseEntity<ApiResponse<ApiCollection>> createCollection(
            @Valid @RequestBody CreateCollectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Collection created", apiTesterService.createCollection(request)));
    }

    @GetMapping("/collections")
    public ResponseEntity<ApiResponse<List<ApiCollection>>> getCollections() {
        return ResponseEntity.ok(ApiResponse.success(apiTesterService.getCollections()));
    }

    @PostMapping("/collections/{collectionId}/requests")
    public ResponseEntity<ApiResponse<ApiRequest>> createRequest(
            @PathVariable Long collectionId,
            @Valid @RequestBody CreateApiRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Request created", apiTesterService.createRequest(collectionId, request)));
    }

    @GetMapping("/collections/{collectionId}/requests")
    public ResponseEntity<ApiResponse<List<ApiRequest>>> getRequests(@PathVariable Long collectionId) {
        return ResponseEntity.ok(ApiResponse.success(apiTesterService.getRequests(collectionId)));
    }

    @PostMapping("/environments")
    public ResponseEntity<ApiResponse<ApiEnvironment>> createEnvironment(
            @Valid @RequestBody CreateEnvironmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Environment created", apiTesterService.createEnvironment(request)));
    }

    @GetMapping("/environments")
    public ResponseEntity<ApiResponse<List<ApiEnvironment>>> getEnvironments() {
        return ResponseEntity.ok(ApiResponse.success(apiTesterService.getEnvironments()));
    }

    @PostMapping("/proxy")
    public ResponseEntity<ApiResponse<ProxyResponse>> proxy(@Valid @RequestBody ProxyRequest request) {
        return ResponseEntity.ok(ApiResponse.success(apiTesterService.proxy(request)));
    }
}
