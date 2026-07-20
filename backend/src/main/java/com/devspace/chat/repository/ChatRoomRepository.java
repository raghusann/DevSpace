package com.devspace.chat.repository;

import com.devspace.chat.entity.ChatMessage;
import com.devspace.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Page<ChatRoom> findByProjectId(Long projectId, Pageable pageable);
}
