package com.medical.webconsultation.controller;

import com.medical.webconsultation.model.ChatMessage;
import com.medical.webconsultation.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatMessageRestController {

    @Autowired
    private ChatMessageRepository chatRepo;

    /**
     * ✅ REST endpoint to get chat history for a consultation
     */
    @GetMapping("/{consultationId}")
    public List<ChatMessage> getMessages(@PathVariable String consultationId) {
        return chatRepo.findByConsultationIdOrderByTimestampAsc(consultationId);
    }
}
