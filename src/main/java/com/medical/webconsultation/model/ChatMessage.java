package com.medical.webconsultation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    @Lob
    @Column(length = 10485760)
    private String content;

    private String consultationId;

    private String fileName;
    private String fileType;

    @JsonProperty("isFile") // ensures correct boolean mapping from JSON
    private boolean isFile;

    private LocalDateTime timestamp;

    // — Getters & Setters with consistent naming
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public boolean isFile() { return isFile; }
    public void setFile(boolean isFile) { this.isFile = isFile; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", isFile=" + isFile +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
