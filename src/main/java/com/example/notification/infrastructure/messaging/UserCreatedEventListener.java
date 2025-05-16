package com.example.notification.infrastructure.messaging;

import com.example.notification.application.NotificationService;
import com.example.notification.domain.UserCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventListener {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public UserCreatedEventListener(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${user.created.queue:user.created}")
    public void handleUserCreatedEvent(String message) {
        try {
            UserCreatedEvent event = objectMapper.readValue(message, UserCreatedEvent.class);
            notificationService.sendUserCreatedNotification(event);
        } catch (Exception e) {
            // Manejo de error de deserialización
            e.printStackTrace();
        }
    }
}
