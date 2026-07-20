package com.devspace.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ChatMessageResponse {

    private Long id;
    private Long roomId;
    private Long senderId;
    private String senderName;
    private String content;
    private Instant createdAt;
}
