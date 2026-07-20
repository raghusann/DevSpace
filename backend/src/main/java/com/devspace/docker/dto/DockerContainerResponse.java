package com.devspace.docker.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DockerContainerResponse {

    private String id;
    private String name;
    private String image;
    private String status;
    private String created;
    private List<String> ports;
}
