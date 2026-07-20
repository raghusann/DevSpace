package com.devspace.apitester.service;

import com.devspace.apitester.dto.CreateApiRequestDto;
import com.devspace.apitester.dto.CreateCollectionRequest;
import com.devspace.apitester.dto.CreateEnvironmentRequest;
import com.devspace.apitester.dto.ProxyRequest;
import com.devspace.apitester.dto.ProxyResponse;
import com.devspace.apitester.entity.ApiCollection;
import com.devspace.apitester.entity.ApiEnvironment;
import com.devspace.apitester.entity.ApiRequest;
import com.devspace.apitester.repository.ApiCollectionRepository;
import com.devspace.apitester.repository.ApiEnvironmentRepository;
import com.devspace.apitester.repository.ApiRequestRepository;
import com.devspace.auth.entity.User;
import com.devspace.auth.repository.UserRepository;
import com.devspace.common.SecurityUtils;
import com.devspace.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiTesterService {

    private final ApiCollectionRepository collectionRepository;
    private final ApiRequestRepository requestRepository;
    private final ApiEnvironmentRepository environmentRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public ApiCollection createCollection(CreateCollectionRequest request) {
        User user = getCurrentUser();
        ApiCollection collection = new ApiCollection();
        collection.setName(request.getName());
        collection.setDescription(request.getDescription());
        collection.setUser(user);
        return collectionRepository.save(collection);
    }

    @Transactional(readOnly = true)
    public List<ApiCollection> getCollections() {
        return collectionRepository.findByUserId(getCurrentUser().getId());
    }

    @Transactional
    public ApiRequest createRequest(Long collectionId, CreateApiRequestDto request) {
        ApiCollection collection = getCollectionForUser(collectionId);
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setCollection(collection);
        apiRequest.setName(request.getName());
        apiRequest.setMethod(request.getMethod().toUpperCase());
        apiRequest.setUrl(request.getUrl());
        apiRequest.setHeaders(request.getHeaders());
        apiRequest.setBody(request.getBody());
        return requestRepository.save(apiRequest);
    }

    @Transactional(readOnly = true)
    public List<ApiRequest> getRequests(Long collectionId) {
        getCollectionForUser(collectionId);
        return requestRepository.findByCollectionId(collectionId);
    }

    @Transactional
    public ApiEnvironment createEnvironment(CreateEnvironmentRequest request) {
        User user = getCurrentUser();
        ApiEnvironment env = new ApiEnvironment();
        env.setName(request.getName());
        env.setVariables(request.getVariables());
        env.setUser(user);
        return environmentRepository.save(env);
    }

    @Transactional(readOnly = true)
    public List<ApiEnvironment> getEnvironments() {
        return environmentRepository.findByUserId(getCurrentUser().getId());
    }

    public ProxyResponse proxy(ProxyRequest request) {
        long start = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        if (request.getHeaders() != null) {
            request.getHeaders().forEach(headers::set);
        }

        HttpEntity<String> entity = new HttpEntity<>(request.getBody(), headers);
        HttpMethod method = HttpMethod.valueOf(request.getMethod().toUpperCase());

        ResponseEntity<String> response = restTemplate.exchange(
                request.getUrl(), method, entity, String.class);

        Map<String, String> responseHeaders = new HashMap<>();
        response.getHeaders().forEach((key, values) -> {
            if (!values.isEmpty()) {
                responseHeaders.put(key, values.getFirst());
            }
        });

        return ProxyResponse.builder()
                .statusCode(response.getStatusCode().value())
                .headers(responseHeaders)
                .body(response.getBody())
                .durationMs(System.currentTimeMillis() - start)
                .build();
    }

    private ApiCollection getCollectionForUser(Long collectionId) {
        ApiCollection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found"));
        if (!collection.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Collection not found");
        }
        return collection;
    }

    private User getCurrentUser() {
        return userRepository.findByEmail(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
