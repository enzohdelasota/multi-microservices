package com.example.notification.domain;

import java.time.LocalDateTime;

public class Notification {
    private Long id;
    private String email;
    private String subject;
    private String body;
    private LocalDateTime sentAt;

    public Notification() {}

    public Notification(Long id, String email, String subject, String body, LocalDateTime sentAt) {
        this.id = id;
        this.email = email;
        this.subject = subject;
        this.body = body;
        this.sentAt = sentAt;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
