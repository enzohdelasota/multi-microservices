package com.example.notification.infrastructure.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "notifications")
public class NotificationDocument {
    @Id
    private String id;
    private String email;
    private String subject;
    private String body;
    private LocalDateTime sentAt;

    public NotificationDocument() {}
    public NotificationDocument(String email, String subject, String body, LocalDateTime sentAt) {
        this.email = email;
        this.subject = subject;
        this.body = body;
        this.sentAt = sentAt;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
