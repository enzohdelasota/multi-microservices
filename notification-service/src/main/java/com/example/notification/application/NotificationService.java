package com.example.notification.application;

import com.example.notification.domain.UserCreatedEvent;

public interface NotificationService {
    void sendUserCreatedNotification(UserCreatedEvent event);
}
