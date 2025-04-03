FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/cloud-file-storage.jar /app/cloud-file-storage.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/cloud-file-storage.jar"]