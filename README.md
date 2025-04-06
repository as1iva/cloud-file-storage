# 🗂️☁️ Проект "Облачное хранилище файлов"
## Описание
Многопользовательское файловое облако. Пользователи сервиса могут использовать его для загрузки и хранения файлов. Проект написан в стиле REST API.

ТЗ проекта – [здесь](https://zhukovsd.github.io/java-backend-learning-course/projects/cloud-file-storage/)

## Использованные технологии / инструменты
### Backend
- Spring Boot
- Spring Security
- Spring Sessions
- Lombok
- Mapstruct
- Gradle
- Swagger

### Database
- Spring Data JPA
- PostgreSQL
- Redis
- Minio
- Liquibase

### Testing
- JUnit 5
- AssertJ
- Testcontainers

### Deploy
- Docker

## Зависимости
- Java 17+
- Docker

## Установка проекта
1. Склонируйте репозиторий
```
git clone git@github.com:as1iva/cloud-file-storage.git
```
2. Откройте папку склонированного репозитория в `Intellij IDEA`
3. Откройте внутри `Intellij IDEA` консоль и пропишите `docker compose up -d`
4. Теперь проект будет доступен по адресу `http://localhost`
