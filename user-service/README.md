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

## Despliegue en Kubernetes

1. Construye la imagen Docker localmente:
   ```zsh
   docker build -t user-service:latest .
   ```
2. Carga la imagen en Minikube:
   ```zsh
   minikube image load user-service:latest
   ```
3. Crea el namespace (opcional):
   ```zsh
   kubectl create namespace microservices
   ```
4. Aplica los manifiestos de Kubernetes:
   ```zsh
   kubectl apply -f k8s-user-service.yaml -n microservices
   ```
5. Verifica los pods:
   ```zsh
   kubectl get pods -n microservices
   ```
6. Consulta logs del pod:
   ```zsh
   kubectl logs <nombre-del-pod> -n microservices
   ```
7. Describe el pod para más detalles:
   ```zsh
   kubectl describe pod <nombre-del-pod> -n microservices
   ```
8. Verifica el servicio:
   ```zsh
   kubectl get svc -n microservices
   ```
9. Obtén la IP de Minikube:
   ```zsh
   minikube ip
   ```
10. Accede al servicio expuesto:
    ```zsh
    minikube service user-service -n microservices
    ```
11. Reinicia el deployment si cambias secrets o configMaps:
    ```zsh
    kubectl rollout restart deployment user-service -n microservices
    ```
12. Elimina los recursos:
    ```zsh
    kubectl delete -f k8s-user-service.yaml -n microservices
    ```

> Asegúrate de tener configurado el Ingress Controller si quieres acceder por dominio (ver manifiesto Ingress en `k8s-user-service.yaml`).
