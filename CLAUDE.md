# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Start local dependencies (PostgreSQL on 5433, Redis on 6379)
docker-compose up -d

# Run the application (port 8081)
./mvnw spring-boot:run

# Build (skip tests)
./mvnw clean verify -DskipTests

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ITLessonsControllerTest

# Run a single test method
./mvnw test -Dtest=LessonsServiceTest#methodName
```

## Architecture Overview

**Spring Boot 3.4.1 / Java 21** multi-tenant driving school booking service.

### Multi-Tenancy (Row-Level / Discriminator)
The core architectural concern. Every HTTP request must include an `X-Tenant-Id` header. The flow:
1. `TenantInterceptor` reads the header and calls `TenantContext.setTenantId()` (ThreadLocal)
2. `TenantIdentifierResolver` (`CurrentTenantIdentifierResolver`) reads from `TenantContext` and provides the tenant value to Hibernate
3. All tenant-scoped entities (`CarEntity`, `InstructorEntity`, `TraineeEntity`, `LessonEntity`) carry a `@TenantId String tenantId` field — Hibernate automatically sets this on persist and filters all queries by it
4. Flyway runs `db/migration/default/` against the `public` schema (all tables live here); `db/migration/tenants/` exists for historical V2/V3 (no longer applied per-tenant)

### Domain Structure
Each business domain (`lessons`, `cars`, `instructors`, `trainees`, `tenants`) follows a consistent layout:
- `model/` — JPA entity
- `domain/` — service-layer business object (maps from entity)
- `controller/` — REST controller with `dto/` for request/response objects
- `service/` — business logic
- `repository/` — Spring Data JPA interface (some with custom criteria-based search)

### Key Cross-Cutting Concerns
- **Exception handling**: `GlobalExceptionHandler` (`@ControllerAdvice`) translates domain exceptions (`ValidationException`, `ResourceNotFoundException`, `DuplicateResourceException`) into consistent `ErrorDetails` responses.
- **OpenAPI**: SpringDoc 2.8.5 — Swagger UI available at `/swagger-ui.html` when running.
- **Logging**: MDC includes `tenantId` for correlation; Hibernate SQL logging is enabled at DEBUG level.

### Database
- PostgreSQL 16 (local: `localhost:5433`, DB: `bookanddrive`, user/pass: `bookanddrive`/`b00kanddrive!`)
- `spring.jpa.hibernate.ddl-auto: validate` — schema is managed exclusively by Flyway, never by Hibernate
- Integration tests spin up a PostgreSQL Testcontainer automatically

### CI/CD
GitHub Actions (`.github/workflows/maven.yml`) runs `mvn clean install` on pushes to `main` and `feat/PLAT*` branches using Java 21 Corretto.
