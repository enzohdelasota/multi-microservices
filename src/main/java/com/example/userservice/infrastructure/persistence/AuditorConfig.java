package com.example.userservice.infrastructure.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditorConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // Aquí puedes obtener el usuario autenticado si tienes seguridad, por ahora "system"
        return () -> Optional.of("system");
    }
}
