# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
mvn clean install

# Run all tests (unit, integration, Flyway DDL validation)
mvn verify

# Run a single test class
mvn test -Dtest=CollegeRepositoryTest

# Run locally
mvn spring-boot:run -Dspring.profiles.active=local
# or after building:
java -jar target/gym-roster.jar --spring.profiles.active=local

# Docker Compose
docker compose up -d
docker compose down

# Database: clean and re-migrate (local only)
mvn flyway:clean -Dspring.profiles.active=local -Dflyway.url=jdbc:postgresql://localhost:5432/gymroster -Dflyway.user=postgres -Dflyway.password=gympass -Dflyway.cleanDisabled=false
mvn flyway:migrate -Dspring.profiles.active=local -Dflyway.url=jdbc:postgresql://localhost:5432/gymroster -Dflyway.user=postgres -Dflyway.password=gympass
```

## Architecture

Gym Roster is a Spring Boot 4 / Java 21 REST API for managing college gymnastics programs — colleges, athletes, coaches, rosters, and meet scores. Data is stored in PostgreSQL 17, versioned with Flyway.

**Layers** (all under `com.gym.roster`):

- **`domain/`** — JPA entities. All extend `BaseEntity` (auto `id`, immutable `creationTimestamp`, auto-updated `lastUpdateTimestamp`). Core entities: `College`, `Athlete`, `Coach`, `AthleteRoster`, `CoachRoster`, `Meet`, `MeetScore`, `MeetScoreDetail`.
- **`controller/`** — REST controllers. Return 200/201/204/404/400. Athlete endpoints return HATEOAS `PagedModel`.
- **`service/`** — Business logic. Services are `@Transactional` and orchestrate repositories and importers.
- **`repository/`** — Spring Data JPA. `AthleteSpecification` handles dynamic multi-field filtering via JPA Criteria API.
- **`parser/`** — CSV file importers. `AbstractImporter<T>` handles directory walking, error tracking, and college caching. Concrete subclasses: `CollegeImporter`, `AthleteRosterImporter`, `CoachRosterImporter`, `MeetImporter`. Each row is processed in its own transaction to isolate failures. Results use `ImportResultStatus`: `UNPROCESSED`, `CREATED`, `UPDATED`, `EXISTS`, `ERROR`.
- **`config/`** — `SecurityConfig`: all endpoints permit-all, CSRF disabled, CORS enabled for localhost:3000/3001 in local profile.
- **`advice/` + `exception/`** — `GlobalExceptionHandler` + `ValidationException` for JSR-380 errors.

**Unique constraints to be aware of:**
- `College`: `codeName`
- `Athlete`: `firstName + lastName + homeCity`
- `Coach`: `firstName + lastName`

**External dependency:** `gym-common` (GitHub package) provides shared enums: `State`, `Country`, `Conference`, `Division`, `Region`, `AcademicYear`, `StaffRole`. Requires a GitHub token in Maven settings to resolve.

## API Documentation

When modifying any controller, endpoint signature, request/response shape, or URL path, update the corresponding document in `docs/api/`. The files map to domain areas:

| File | Covers |
|------|--------|
| `docs/api/api-college.md` | `/college` endpoints |
| `docs/api/api-athlete.md` | `/athlete` endpoints |
| `docs/api/api-coach.md` | `/coach` endpoints |
| `docs/api/api-roster.md` | `/roster/athlete` and `/roster/coach` endpoints |
| `docs/api/api-meet.md` | `/meet` endpoints |
