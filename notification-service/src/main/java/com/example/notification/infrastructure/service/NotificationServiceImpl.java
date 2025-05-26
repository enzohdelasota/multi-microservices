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
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender mailSender;
    private final NotificationMongoRepository notificationMongoRepository;
    private final NotificationTemplateRepository templateRepository;

    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private String mailPort;
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
        log.info("Configuración de correo: host={}, puerto={}, usuario={}", mailHost, mailPort, fromEmail);
        log.info("Iniciando envío de notificación para usuario: {}", event.getEmail());
        try {
            var templateOpt = templateRepository.findByType("USER_CREATED");
            if (templateOpt.isEmpty()) {
                log.error("No existe template para USER_CREATED");
                throw new RuntimeException("No existe template para USER_CREATED");
            }
            var template = templateOpt.get();
            log.debug("Template encontrado: asunto='{}'", template.getSubject());
            var mimeMessage = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(event.getEmail());
            String subject = template.getSubject().replace("{{name}}", event.getName());
            helper.setSubject(subject);
            String html = template.getBody().replace("{{name}}", event.getName());
            helper.setText(html, true);
            log.info("Enviando correo a {} con asunto '{}'", event.getEmail(), subject);
            mailSender.send(mimeMessage);
            log.info("Correo enviado exitosamente a {}", event.getEmail());

            // Registrar notificación en MongoDB
            NotificationDocument doc = new NotificationDocument(event.getEmail(), subject, html, LocalDateTime.now());
            notificationMongoRepository.save(doc);
            log.info("Notificación almacenada en MongoDB para usuario: {}", event.getEmail());
        } catch (MessagingException e) {
            log.error("Error enviando correo HTML a {}: {}", event.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Error enviando correo HTML", e);
        } catch (Exception e) {
            log.error("Error general en el flujo de notificación para {}: {}", event.getEmail(), e.getMessage(), e);
            throw e;
        }
    }
}
