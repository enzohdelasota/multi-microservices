# User Service

Este microservicio permite el alta de usuarios, persiste la información en PostgreSQL y publica eventos `user.created` en RabbitMQ. Utiliza Spring Boot 3.4.5, Java 24, Maven y arquitectura hexagonal.

## Características
- API REST para alta de usuarios
- Persistencia en PostgreSQL
- Publicación de eventos en RabbitMQ
- Arquitectura hexagonal

## Requisitos
- Java 24
- Maven
- PostgreSQL
- RabbitMQ

## Ejecución
1. Configura las variables de entorno para la base de datos y RabbitMQ en `src/main/resources/application.properties`.
2. Ejecuta:
   ```zsh
   mvn spring-boot:run
   ```

## Estructura del proyecto
- `domain`: Lógica de negocio
- `application`: Casos de uso
- `infrastructure`: Adaptadores (REST, JPA, RabbitMQ)

## Autor
Generado con GitHub Copilot
