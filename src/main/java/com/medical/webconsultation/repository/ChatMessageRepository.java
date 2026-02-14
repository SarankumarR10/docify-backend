package com.medical.webconsultation.repository;

import com.medical.webconsultation.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 🔄 Get all messages by consultation ID, ordered by timestamp ASC
    List<ChatMessage> findByConsultationIdOrderByTimestampAsc(String consultationId);
}
