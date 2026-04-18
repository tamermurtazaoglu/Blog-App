# Blog App

A RESTful Blog API built with Spring Boot, evolved from a monolith into a microservice architecture across four versions.

## Architecture

```
┌─────────────────┐        ┌──────────────────┐
│   user-service  │        │   blog-service   │
│   (port 8080)   │        │   (port 8081)    │
│                 │        │                  │
│  - Register     │  JWT   │  - Posts (CRUD)  │
│  - Login        │──────▶ │  - Tags          │
│  - Delete user  │        │  - Media upload  │
│                 │        │  - Full-text     │
│                 │        │    search        │
└────────┬────────┘        └────────┬─────────┘
         │                          │
         │   UserDeletedEvent        │
         └──────────┬───────────────┘
                    ▼
              ┌──────────┐
              │  Kafka   │
              └──────────┘
```

Each service has its own database. Services do not call each other over HTTP. When a user is deleted, `user-service` publishes a `UserDeletedEvent` to Kafka; `blog-service` consumes it and deletes all posts belonging to that user.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.4.0 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| ORM | Spring Data JPA / Hibernate |
| Database | H2 (test), MySQL 9.2 (prod) |
| Messaging | Apache Kafka |
| Search | Hibernate Search 7.2 + Lucene |
| Media | Thumbnailator 0.4 (multi-resolution image variants) |
| Build | Maven |
| Test | JUnit 5, Mockito, TestContainers |
| Docs | SpringDoc OpenAPI (Swagger UI) |
| Code Quality | SonarQube 10.4, JaCoCo 0.8 |
| Container | Docker, Docker Compose |
| CI/CD | GitHub Actions, GHCR |

## Services

### user-service

Handles authentication and user lifecycle.

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/users/create` | Public | Register |
| POST | `/users/login` | Public | Login — returns JWT |
| POST | `/users/logout` | Public | Logout |
| DELETE | `/users/{id}` | Authenticated | Delete user + publish Kafka event |

JWT tokens embed the `userId` claim so `blog-service` can verify post ownership without a user database lookup.

### blog-service

Handles all content. Post ownership is tracked by `userId: Long` (no foreign key — users live in a separate database).

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/posts` | Public | All posts (paginated) |
| GET | `/posts/{id}` | Public | Post detail |
| GET | `/posts/byTag/{tag}` | Public | Posts by tag (paginated) |
| GET | `/posts/search?q=` | Public | Full-text search |
| POST | `/posts` | Authenticated | Create post |
| PUT | `/posts/{id}` | Authenticated | Update post (owner only) |
| DELETE | `/posts/{id}` | Authenticated | Delete post (owner only) |
| POST | `/posts/{id}/media` | Authenticated | Upload image/video |
| DELETE | `/posts/{id}/media/{groupId}` | Authenticated | Delete media |
| GET | `/media/{id}` | Public | Download media file |

Image uploads are stored in four resolution variants (ORIGINAL, LARGE 1280px, MEDIUM 640px, SMALL 320px) using Thumbnailator. All variants share a `groupId` for batch deletion.

## Running Locally

### With Docker Compose

```bash
cp .env.example .env
# Fill in the values in .env
docker compose up -d
```

- user-service: http://localhost:8080/swagger-ui/index.html
- blog-service:  http://localhost:8081/swagger-ui/index.html
- SonarQube:     http://localhost:9000

### Without Docker (H2, test profile)

```bash
# user-service
cd services/user-service
mvn spring-boot:run

# blog-service
cd services/blog-service
mvn spring-boot:run
```

## CI/CD

GitHub Actions runs tests for both services on every PR and push.  
On merge to `main`, Docker images are built and pushed to GHCR.

## Project Evolution

| Version | What was added |
|---|---|
| v0.1.0 | Monolith — Users, Posts, Tags, JWT auth |
| v0.2.0 | Docker, MySQL, Spring profiles |
| fix/v0.2.0 | Security hardening (JWT key, ownership checks, BCrypt singleton) |
| v0.3.0 | Pagination, SonarQube/JaCoCo, Hibernate Search, Media upload |
| v0.4.0 | Microservice split, Kafka event-driven delete, GitHub Actions CI/CD |
