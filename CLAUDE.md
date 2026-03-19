# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Coding Standards

Before writing or modifying any code in this repository, always run the `/java-developer` command and follow the coding standards and best practices it defines.

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
The core architectural concern. Tenant ID is resolved from the Clerk JWT — no `X-Tenant-Id` header. The flow:
1. Spring Security validates the JWT against Clerk's JWKS endpoint (`SecurityConfig`)
2. `ClerkJwtTenantResolver` extracts the org ID from the `o.id` field in the JWT `o` claim
3. `TenantInterceptor` calls the resolver and sets the tenant via `TenantContext.setTenantId()` (ThreadLocal); returns 403 if no org is present in the token
4. `TenantIdentifierResolver` (`CurrentTenantIdentifierResolver`) reads from `TenantContext` and provides the value to Hibernate; falls back to `"public"` for non-request contexts (Hibernate startup)
5. All tenant-scoped entities (`CarEntity`, `InstructorEntity`, `TraineeEntity`, `LessonEntity`) carry a `@TenantId String tenantId` field — Hibernate automatically sets this on persist and filters all queries by it
6. Flyway runs `db/migration/default/` against the `public` schema (all tables live here); `db/migration/tenants/` exists for historical V2/V3 (no longer applied per-tenant)

**Integration tests:** use `@ActiveProfiles("integration-test")` on every IT test class. This swaps in `SecurityTestConfig` (permits all requests, returns fixed tenant `"public"`) and excludes `OAuth2ResourceServerAutoConfiguration` so tests don't try to reach Clerk's JWKS endpoint.

### Domain Structure
Each business domain (`lessons`, `cars`, `instructors`, `trainees`, `tenants`) follows a consistent layout:
- `model/` — JPA entity
- `domain/` — service-layer business object (maps from entity)
- `controller/` — REST controller with `dto/` for request/response objects
- `service/` — business logic
- `repository/` — Spring Data JPA interface (some with custom criteria-based search)

### Key Cross-Cutting Concerns
- **Exception handling**: `GlobalExceptionHandler` (`@ControllerAdvice`) translates domain exceptions (`ValidationException`, `ResourceNotFoundException`, `DuplicateResourceException`) into consistent `ErrorDetails` responses.
- **OpenAPI**: SpringDoc 2.8.5 — Swagger UI at `/swagger-ui.html`. Requires a valid Clerk JWT — use the "Authorize" button and paste a Bearer token.
- **Logging**: MDC includes `tenantId` for correlation; Hibernate SQL logging is enabled at DEBUG level.

### Database
- PostgreSQL 16 (local: `localhost:5433`, DB: `bookanddrive`, user/pass: `bookanddrive`/`b00kanddrive!`)
- `spring.jpa.hibernate.ddl-auto: validate` — schema is managed exclusively by Flyway, never by Hibernate
- Integration tests spin up a PostgreSQL Testcontainer automatically

### CI/CD
GitHub Actions (`.github/workflows/maven.yml`) runs `mvn clean install` on pushes to `main` and `feat/PLAT*` branches using Java 21 Corretto.
