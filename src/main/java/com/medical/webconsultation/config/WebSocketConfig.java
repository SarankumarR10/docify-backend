package com.medical.webconsultation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * WebSocketConfig enables STOMP-based WebSocket communication
 * for real-time doctor-patient video/chat consultation.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures message broker:
     * - "/topic": destination prefix for subscribers (e.g., chat, signal, presence)
     * - "/app": application prefix for @MessageMapping endpoints
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");               // Broadcast to subscribers
        config.setApplicationDestinationPrefixes("/app");  // Inbound messages (from client)
    }

    /**
     * Registers STOMP endpoint for WebSocket handshake.
     * - "/ws" is the entry point
     * - SockJS fallback enabled for browser compatibility
     * - Allows cross-origin WebSocket connections
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")                         // WebSocket endpoint
                .setAllowedOriginPatterns("*")              // Allow all origins (use specific domains in production)
                .withSockJS();                              // Enable SockJS fallback
    }
}
