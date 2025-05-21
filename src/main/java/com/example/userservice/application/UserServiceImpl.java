package com.example.userservice.application;

import com.example.userservice.domain.User;
import com.example.userservice.infrastructure.persistence.UserEntity;
import com.example.userservice.infrastructure.persistence.UserMapper;
import com.example.userservice.infrastructure.persistence.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        Optional<UserEntity> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado por otro usuario");
        }
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = userRepository.save(entity);
        User created = UserMapper.toDomain(saved);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String userJson = objectMapper.writeValueAsString(created);
            rabbitTemplate.convertAndSend(userCreatedQueue, userJson);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando usuario para RabbitMQ", e);
        }
        return created;
    }
}
