package com.example.userservice.application;

import com.example.userservice.domain.User;
import com.example.userservice.infrastructure.persistence.UserEntity;
import com.example.userservice.infrastructure.persistence.UserMapper;
import com.example.userservice.infrastructure.persistence.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final String userCreatedQueue;

    public UserServiceImpl(UserRepository userRepository, RabbitTemplate rabbitTemplate,
                          @Value("${user.created.queue:user.created}") String userCreatedQueue) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.userCreatedQueue = userCreatedQueue;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        log.debug("Validando unicidad de email: {}", user.getEmail());
        Optional<UserEntity> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            log.warn("Intento de alta con email ya existente: {}", user.getEmail());
            throw new IllegalArgumentException("El correo ya está registrado por otro usuario");
        }
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = userRepository.save(entity);
        User created = UserMapper.toDomain(saved);
        try {
            String correlationId = MDC.get("X-Correlation-ID");
            ObjectMapper objectMapper = new ObjectMapper();
            String userJson = objectMapper.writeValueAsString(created);
            MessageProperties props = new MessageProperties();
            if (correlationId != null) {
                props.setHeader("X-Correlation-ID", correlationId);
            }
            Message message = new Message(userJson.getBytes(), props);
            log.info("Publicando evento user.created en RabbitMQ para usuario id: {} con correlationId: {}", created.getId(), correlationId);
            rabbitTemplate.send(userCreatedQueue, message);
        } catch (Exception e) {
            log.error("Error serializando usuario para RabbitMQ", e);
            throw new RuntimeException("Error serializando usuario para RabbitMQ", e);
        }
        log.info("Usuario persistido y evento publicado: {}", created.getEmail());
        return created;
    }
}
