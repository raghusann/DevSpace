package com.devspace.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateChatRoomRequest {

    @NotBlank(message = "Room name is required")
    private String name;

    private boolean isGroup = true;
}
