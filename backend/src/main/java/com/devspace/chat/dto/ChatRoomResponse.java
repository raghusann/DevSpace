package com.devspace.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ChatRoomResponse {

    private Long id;
    private String name;
    private boolean isGroup;
    private Long projectId;
    private Instant createdAt;
}
