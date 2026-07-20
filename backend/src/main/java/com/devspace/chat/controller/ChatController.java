package com.devspace.chat.controller;

import com.devspace.common.ApiResponse;
import com.devspace.common.PageResponse;
import com.devspace.chat.dto.ChatMessageResponse;
import com.devspace.chat.dto.ChatRoomResponse;
import com.devspace.chat.dto.CreateChatRoomRequest;
import com.devspace.chat.dto.SendMessageRequest;
import com.devspace.chat.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects/{projectId}/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/rooms")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> createRoom(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateChatRoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Chat room created", chatService.createRoom(projectId, request)));
    }

    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<PageResponse<ChatRoomResponse>>> getRooms(
            @PathVariable Long projectId,
            Pageable pageable) {
        Page<ChatRoomResponse> page = chatService.getRooms(projectId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(page)));
    }

    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            @PathVariable Long projectId,
            @PathVariable Long roomId,
            @Valid @RequestBody SendMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Message sent", chatService.sendMessage(roomId, request)));
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ApiResponse<PageResponse<ChatMessageResponse>>> getMessages(
            @PathVariable Long projectId,
            @PathVariable Long roomId,
            Pageable pageable) {
        Page<ChatMessageResponse> page = chatService.getMessages(roomId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(page)));
    }
}
