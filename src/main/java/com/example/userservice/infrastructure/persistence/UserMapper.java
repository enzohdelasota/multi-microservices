package com.example.userservice.infrastructure.persistence;

import com.example.userservice.domain.User;

public class UserMapper {
    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        return user;
    }
}
