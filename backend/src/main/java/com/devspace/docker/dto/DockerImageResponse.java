package com.devspace.docker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DockerImageResponse {

    private String id;
    private String name;
    private String tag;
    private String size;
    private String created;
}
