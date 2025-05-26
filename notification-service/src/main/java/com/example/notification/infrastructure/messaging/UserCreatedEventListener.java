package com.example.notification.infrastructure.messaging;

import com.example.notification.application.NotificationService;
import com.example.notification.domain.UserCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCreatedEventListener {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public UserCreatedEventListener(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${user.created.queue:user.created}")
    public void handleUserCreatedEvent(String message, @Header(name = "X-Correlation-ID", required = false) String correlationId) {
        try {
            if (correlationId != null) {
                MDC.put("X-Correlation-ID", correlationId);
                log.info("[{}] Recibido evento user.created con correlationId: {}", java.time.LocalDateTime.now(), correlationId);
            } else {
                log.info("[{}] Recibido evento user.created sin correlationId", java.time.LocalDateTime.now());
            }
            UserCreatedEvent event = objectMapper.readValue(message, UserCreatedEvent.class);
            log.info("Procesando notificación para usuario: {}", event.getEmail());
            notificationService.sendUserCreatedNotification(event);
            log.info("Notificación enviada para usuario: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Error deserializando evento user.created. correlationId: {}. Error: {}", correlationId, e.getMessage(), e);
        } finally {
            MDC.remove("X-Correlation-ID");
        }
    }
}
