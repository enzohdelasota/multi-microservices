FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/user-service-*.jar app.jar

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "app.jar"]
