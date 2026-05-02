# fluent-repo-4j-demo

This project is a minimal Spring Boot demo for [`fluent-repo-4j`](https://github.com/auspis/fluent-repo-4j).

Its goal is to show a classic `controller -> service -> repository` architecture on top of a simple `User` domain, while keeping the setup easy to run locally. The application exposes a small CRUD API for users and includes Swagger UI so the demo can be explored directly from the browser.

## What This Demo Shows

- Spring Boot 3.5.13 application structure
- `fluent-repo-4j` repository usage with a `User` entity
- REST CRUD endpoints for creating, reading, updating, and deleting users
- Swagger UI for browser-based interaction
- Automatic MySQL startup with Docker through Testcontainers

## Tech Stack

- Java 21
- Spring Boot 3.5.14
- `fluent-repo-4j` 1.3.0
- MySQL 8
- Testcontainers
- Swagger UI via `springdoc-openapi`

## Prerequisites

Make sure the following are available on your machine:

- Git
- Java 21
- Docker

Docker must be running before you start the application, because the demo starts a MySQL container automatically.

## Run The Project

Clone the repository:

```bash
git clone https://github.com/auspis/fluent-repo-4j-demo.git
cd fluent-repo-4j-demo
```

Start the application with the Maven Wrapper:

```bash
./mvnw spring-boot:run
# to enable statement log
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dfluentsql.hooks.build.logging.enabled=true -Dfluentsql.hooks.build.logging.includeParams=true"
```

What happens at startup:

- A MySQL 8 container is started with Docker
- Spring Boot connects to that container automatically
- The `users` table is created
- A few demo users are inserted as seed data

## URLs

Once the application is running, open:

- Swagger UI: [`http://localhost:9023/swagger-ui.html`](http://localhost:9023/swagger-ui.html)
- OpenAPI JSON: [`http://localhost:9023/v3/api-docs`](http://localhost:9023/v3/api-docs)

The user API is exposed under:

- [`http://localhost:9023/api/users`](http://localhost:9023/api/users)

## Example Endpoints

- `GET /api/users`
- `GET /api/users/{id}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

## Notes

- The database is ephemeral and tied to the application lifecycle.
- When the application stops, the MySQL container is stopped as well.
- This project is intended as a runnable demo, not as a production-ready template.