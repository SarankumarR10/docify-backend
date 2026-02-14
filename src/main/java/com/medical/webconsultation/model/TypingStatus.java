package com.medical.webconsultation.model;

/**
 * TypingStatus represents a real-time status indicating whether a user is typing
 * in a live consultation room.
 */
public class TypingStatus {

    private String sender;  // Who is typing (user name or ID)
    private boolean typing; // true = typing, false = not typing

    // 🔹 Default constructor
    public TypingStatus() {
    }

    // 🔹 Parameterized constructor
    public TypingStatus(String sender, boolean typing) {
        this.sender = sender;
        this.typing = typing;
    }

    // 🔹 Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isTyping() {
        return typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }
}
