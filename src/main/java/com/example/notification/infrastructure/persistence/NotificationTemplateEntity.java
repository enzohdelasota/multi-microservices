package com.example.notification.infrastructure.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "notification_templates")
public class NotificationTemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String type;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    public NotificationTemplateEntity() {}
    public NotificationTemplateEntity(String type, String subject, String body) {
        this.type = type;
        this.subject = subject;
        this.body = body;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
