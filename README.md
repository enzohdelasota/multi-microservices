# Notification Service

Microservicio para el envío de notificaciones por correo electrónico.

- **Framework:** Spring Boot 3.4.5
- **Java:** 21
- **Mensajería:** RabbitMQ
- **Base de datos:** PostgreSQL
- **Arquitectura:** Hexagonal

## Funcionalidad
- Consume eventos de usuario creado desde una cola RabbitMQ (JSON).
- Envía notificaciones por correo electrónico.
- Registra las notificaciones enviadas en PostgreSQL.

## Ejecución
1. Configura las variables de entorno y el `application.properties` para RabbitMQ, correo y PostgreSQL.
2. Ejecuta con:
   ```sh
   ./mvnw spring-boot:run
   ```

## Estructura Hexagonal
- `domain`: Lógica de negocio
- `application`: Casos de uso
- `infrastructure`: Integraciones externas (RabbitMQ, correo, base de datos)
- `adapter`: Adaptadores de entrada/salida
