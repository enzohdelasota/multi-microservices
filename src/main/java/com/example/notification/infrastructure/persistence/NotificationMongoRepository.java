package com.example.notification.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationMongoRepository extends MongoRepository<NotificationDocument, String> {
}
