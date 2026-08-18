# ADR-001: Tech stack choice

## Decision

Use Java 25 + Spring Boot 4 for the Booking Platform backend.

## Context

Goal is a CV-ready, interview-ready backend demonstrating:

- REST APIs, validation, error handling
- RBAC security
- Idempotency + concurrency control
- Integration testing with real PostgreSQL

## Consequences

- Strong enterprise credibility for backend roles
- Slightly more setup than FastAPI, but better for Spring/JPA/security stories
