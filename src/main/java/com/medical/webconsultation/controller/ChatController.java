package com.medical.webconsultation.controller;

import com.medical.webconsultation.model.ChatMessage;
import com.medical.webconsultation.model.TypingStatus;
import com.medical.webconsultation.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository chatRepo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * ✅ WebRTC signaling relay between doctor and patient.
     */
    @MessageMapping("/signal/{consultationId}")
    @SendTo("/topic/signal/{consultationId}")
    public String relaySignal(@DestinationVariable String consultationId,
                              @Payload String signalData) {
        return signalData;
    }

    /**
     * ✅ Chat message handler for text & files
     */
    @MessageMapping("/chat/{consultationId}")
    @SendTo("/topic/chat/{consultationId}")
    public ChatMessage sendMessage(@DestinationVariable String consultationId,
                                   @Payload ChatMessage chatMessage) {

        chatMessage.setConsultationId(consultationId);
        chatMessage.setTimestamp(LocalDateTime.now());

        // Debug logging for incoming message
        System.out.println("📩 Incoming ChatMessage: " + chatMessage);

        boolean isFile = chatMessage.isFile(); // safe interpretation
        chatMessage.setFile(isFile); // ensure proper boolean mapping

        if (isFile) {
            if (chatMessage.getFileName() == null || chatMessage.getFileName().trim().isEmpty()) {
                chatMessage.setFileName("UnnamedFile");
            }
            if (chatMessage.getFileType() == null || chatMessage.getFileType().trim().isEmpty()) {
                chatMessage.setFileType("application/octet-stream");
            }
        } else {
            chatMessage.setFileName(null);
            chatMessage.setFileType(null);
        }

        ChatMessage savedMessage = chatRepo.save(chatMessage);

        // Debug logging after saving
        System.out.println("✅ Saved ChatMessage: " + savedMessage);

        return savedMessage;
    }

    /**
     * ✅ Typing indicator support
     */
    @MessageMapping("/chat/{consultationId}/typing")
    @SendTo("/topic/chat/{consultationId}/typing")
    public TypingStatus broadcastTypingStatus(@DestinationVariable String consultationId,
                                              @Payload TypingStatus typingStatus) {
        return typingStatus;
    }

    /**
     * ✅ Presence (online/offline) relay
     */
    @MessageMapping("/presence/{consultationId}")
    public void handlePresence(@DestinationVariable String consultationId,
                               @Payload String presencePayload) {
        messagingTemplate.convertAndSend("/topic/presence/" + consultationId, presencePayload);
    }

    /**
     * 🟡 Public/global chat - optional
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendPublicMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUserToPublic(@Payload ChatMessage chatMessage,
                                       SimpMessageHeaderAccessor accessor) {
        accessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
