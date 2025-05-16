package com.example.notification.infrastructure.service;

import com.example.notification.application.NotificationService;
import com.example.notification.domain.UserCreatedEvent;
import com.example.notification.infrastructure.persistence.NotificationMongoRepository;
import com.example.notification.infrastructure.persistence.NotificationTemplateRepository;
import com.example.notification.infrastructure.persistence.NotificationDocument;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender mailSender;
    private final NotificationMongoRepository notificationMongoRepository;
    private final NotificationTemplateRepository templateRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public NotificationServiceImpl(JavaMailSender mailSender, NotificationMongoRepository notificationMongoRepository, NotificationTemplateRepository templateRepository) {
        this.mailSender = mailSender;
        this.notificationMongoRepository = notificationMongoRepository;
        this.templateRepository = templateRepository;
    }

    @Override
    @Transactional
    public void sendUserCreatedNotification(UserCreatedEvent event) {
        try {
            var templateOpt = templateRepository.findByType("USER_CREATED");
            if (templateOpt.isEmpty()) {
                throw new RuntimeException("No existe template para USER_CREATED");
            }
            var template = templateOpt.get();
            var mimeMessage = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(event.getEmail());
            String subject = template.getSubject().replace("{{name}}", event.getName());
            helper.setSubject(subject);
            String html = template.getBody().replace("{{name}}", event.getName());
            helper.setText(html, true);
            mailSender.send(mimeMessage);

            // Registrar notificación en MongoDB
            NotificationDocument doc = new NotificationDocument(event.getEmail(), subject, html, LocalDateTime.now());
            notificationMongoRepository.save(doc);
        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando correo HTML", e);
        }
    }
}
