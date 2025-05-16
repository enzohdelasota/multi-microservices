package com.example.userservice.infrastructure.persistence;

import com.example.userservice.infrastructure.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
