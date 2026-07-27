# English Academy

[![CI](https://github.com/LivailleDev/english-academy/actions/workflows/ci.yml/badge.svg)](https://github.com/LivailleDev/english-academy/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A full-stack English course platform: a Spring Boot REST API backed by MySQL, consumed by a React/TypeScript frontend. Browse courses by CEFR level, look at their lessons, and enroll.

Monorepo: `/backend` (Spring Boot) and `/frontend` (React).

---

## Domain

- **Course** — title, description, CEFR level (A1–C2), duration.
- **Lesson** — belongs to a course, ordered.
- **Student** — name + unique email.
- **Enrollment** — the student↔course join, carrying its own state (`IN_PROGRESS` / `COMPLETED`) and enrollment date, modeled as its own entity rather than a plain many-to-many, so it can grow (progress %, completion date, etc.) without a schema rewrite.

## Backend

*Java 17 · Spring Boot 3 · Spring Data JPA · MySQL · Flyway · Testcontainers · Maven*

- Layered architecture: **Controller → Service → Repository**, DTOs (Java `record`s) at the REST boundary — the JPA entities never leave the domain.
- Schema managed by **Flyway** migrations (`ddl-auto=validate`, never `update`).
- Errors returned as **RFC 7807** `application/problem+json` via Spring's built-in `ProblemDetail`.
- Input validated with Bean Validation (`jakarta.validation`).
- Unit tests (Mockito) for service logic + integration tests (`*IT`, Testcontainers) exercising the full stack against a real MySQL instance, run via `mvn verify` (Failsafe).

### Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/courses` | List courses |
| GET | `/api/courses/{id}` | Course detail, with lessons |
| POST | `/api/courses` | Create a course |
| PUT | `/api/courses/{id}` | Update a course |
| DELETE | `/api/courses/{id}` | Delete a course |
| POST | `/api/courses/{id}/lessons` | Add a lesson to a course |
| POST | `/api/students` | Register a student |
| POST | `/api/enrollments` | Enroll a student in a course |
| GET | `/api/students/{id}/enrollments` | List a student's enrollments |

## Frontend

*React 19 · TypeScript · Vite*

- Course list → course detail (with lessons) → inline enrollment form.
- API layer isolated under `src/api`, typed end-to-end against the backend's DTOs.
- Plain `useState`/`useEffect`, no external state library — the app is small enough not to need one.

---

## Running locally

```bash
docker compose up -d                        # start MySQL

cd backend && mvn spring-boot:run            # API on :8080
cd frontend && npm install && npm run dev    # UI on :5173
```

Run the backend test suite:

```bash
cd backend
mvn test      # unit tests
mvn verify    # unit + integration tests (needs Docker for Testcontainers)
```

---

## Tech stack

Java 17 · Spring Boot · MySQL · Flyway · Testcontainers · React · TypeScript · Vite · GitHub Actions

## License

[MIT](LICENSE)
