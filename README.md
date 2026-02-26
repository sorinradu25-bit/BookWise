# BookWise (Booking Platform API)

Backend-first booking API built with Java 21 + Spring Boot + PostgreSQL + Flyway.

## Goals

- Providers publish availability slots
- Users can book a slot safely (idempotency + concurrency handling)
- Predictable API (validation + standardized errors)
- Secured endpoints with RBAC (USER / PROVIDER / ADMIN)

## Tech

- Java 21, Spring Boot 3
- PostgreSQL (Docker)
- Flyway migrations
- OpenAPI/Swagger (later)
- Testcontainers (later)

## Run locally (later)

- docker compose up -d
- ./mvnw spring-boot:run
