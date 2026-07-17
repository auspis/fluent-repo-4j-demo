# fluent-repo-4j-demo

This project is a minimal Spring Boot demo for [`fluent-repo-4j`](https://github.com/auspis/fluent-repo-4j).

Its goal is to compare two repository styles side-by-side while keeping setup easy to run locally:

- `Users` domain: classic OOP style (`CrudRepository`)
- `Plants` domain: functional style (`FunctionalCrudRepository`)

The application includes Swagger UI so the API contracts can be explored directly from the browser.

## What This Demo Shows

- Spring Boot 3.5.14 application structure
- Two approaches in one codebase: OOP and functional repository style
- REST CRUD endpoints for users and plants
- Swagger UI for browser-based interaction
- Automatic MySQL startup with Docker through Testcontainers

## Tech Stack

- Java 21
- Spring Boot 3.5.15
- `fluent-repo-4j` 1.4.2
- MySQL 8
- Testcontainers
- Swagger UI via `springdoc-openapi`

## Design Comparison

### Users (OOP style)

- Repository extends `CrudRepository<User, Long>`
- Service flow uses classic Spring Data semantics (`Optional`, `Iterable`)
- Endpoints under `/api/users`

### Plants (Functional style)

- Repository extends `FunctionalCrudRepository<Plant, Long>`
- Read operations use `ReadResult` (`Found`, `NotFound`, `Error`)
- Write operations use `WriteResult` (`Success`, `Error`)
- Service maps result states to HTTP responses
- Endpoints under `/api/plants`

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
- The `plants` table is created
- A few demo users and plants are inserted as seed data

## URLs

Once the application is running, open:

- Swagger UI: [`http://localhost:9023/swagger-ui.html`](http://localhost:9023/swagger-ui.html)
- OpenAPI JSON: [`http://localhost:9023/v3/api-docs`](http://localhost:9023/v3/api-docs)

The user API is exposed under:

- [`http://localhost:9023/api/users`](http://localhost:9023/api/users)

The plant API is exposed under:

- [`http://localhost:9023/api/plants`](http://localhost:9023/api/plants)

## Example Endpoints

Users (OOP):

- `GET /api/users`
- `GET /api/users/{id}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

Plants (Functional):

- `GET /api/plants`
- `GET /api/plants/{id}`
- `GET /api/plants/watering-less-than?days=7`
- `POST /api/plants`
- `PUT /api/plants/{id}`
- `DELETE /api/plants/{id}`

## Notes

- The database is ephemeral and tied to the application lifecycle.
- When the application stops, the MySQL container is stopped as well.
- This project is intended as a runnable demo, not as a production-ready template.