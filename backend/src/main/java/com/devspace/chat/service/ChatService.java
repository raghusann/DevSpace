package com.devspace.chat.service;

import com.devspace.auth.entity.User;
import com.devspace.auth.repository.UserRepository;
import com.devspace.common.SecurityUtils;
import com.devspace.common.exception.ResourceNotFoundException;
import com.devspace.chat.dto.ChatMessageResponse;
import com.devspace.chat.dto.ChatRoomResponse;
import com.devspace.chat.dto.CreateChatRoomRequest;
import com.devspace.chat.dto.SendMessageRequest;
import com.devspace.chat.entity.ChatMessage;
import com.devspace.chat.entity.ChatRoom;
import com.devspace.chat.repository.ChatMessageRepository;
import com.devspace.chat.repository.ChatRoomRepository;
import com.devspace.project.entity.Project;
import com.devspace.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoomResponse createRoom(Long projectId, CreateChatRoomRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ChatRoom room = new ChatRoom();
        room.setName(request.getName());
        room.setGroup(request.isGroup());
        room.setProject(project);
        room = chatRoomRepository.save(room);

        return mapRoom(room);
    }

    @Transactional(readOnly = true)
    public Page<ChatRoomResponse> getRooms(Long projectId, Pageable pageable) {
        return chatRoomRepository.findByProjectId(projectId, pageable).map(this::mapRoom);
    }

    @Transactional
    public ChatMessageResponse sendMessage(Long roomId, SendMessageRequest request) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat room not found"));
        User sender = getCurrentUser();

        ChatMessage message = new ChatMessage();
        message.setRoom(room);
        message.setSender(sender);
        message.setContent(request.getContent());
        message = chatMessageRepository.save(message);

        return mapMessage(message);
    }

    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> getMessages(Long roomId, Pageable pageable) {
        return chatMessageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageable)
                .map(this::mapMessage);
    }

    private User getCurrentUser() {
        return userRepository.findByEmail(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private ChatRoomResponse mapRoom(ChatRoom room) {
        return ChatRoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .isGroup(room.isGroup())
                .projectId(room.getProject() != null ? room.getProject().getId() : null)
                .createdAt(room.getCreatedAt())
                .build();
    }

    private ChatMessageResponse mapMessage(ChatMessage message) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .roomId(message.getRoom().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getFirstName() + " " + message.getSender().getLastName())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
