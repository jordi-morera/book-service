# Book Service

*[Leer en español](README.es.md)*

A base microservice built with **Java 25** and **Spring Boot 3.5**, designed as a clean,
best-practice starting point to be audited later by AI agents
(security, quality, architecture).

## Stack

- Java 25 (Records, Text Blocks, Pattern Matching where applicable)
- Spring Boot 3.5 (Web, Data JPA, Security 6, Validation, Actuator)
- In-memory H2
- springdoc-openapi (Swagger UI)
- Maven

## Architecture

```
Controller -> Service (interface + impl) -> Repository -> Entity
DTOs (Records) for request/response, decoupled from the JPA entity
GlobalExceptionHandler (@RestControllerAdvice) for uniform errors
SecurityConfig (SecurityFilterChain) for authentication/authorization
```

## Running the project

Requirements: JDK 25 and Maven (or use the `./mvnw` wrapper if you generate it with `mvn -N wrapper:wrapper`).

```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

## Demo credentials (HTTP Basic)

- User: `admin`
- Password: `admin123`

All endpoints under `/api/v1/books/**` require authentication.
The `GET /api/v1/public/status` endpoint is public (no authentication), as an example.

## Swagger / OpenAPI

- UI: http://localhost:8080/swagger-ui.html
- Contract JSON: http://localhost:8080/v3/api-docs

## H2 Console

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:bookdb`
- User: `sa`
- Password: *(empty)*

## Main endpoints

| Method | Path                     | Auth   | Description               |
|--------|--------------------------|--------|---------------------------|
| GET    | /api/v1/public/status    | No     | Public health check       |
| GET    | /api/v1/books            | Basic  | List all books            |
| GET    | /api/v1/books/{id}       | Basic  | Get a book                |
| POST   | /api/v1/books            | Basic  | Create a book             |
| PUT    | /api/v1/books/{id}       | Basic  | Update a book             |
| DELETE | /api/v1/books/{id}       | Basic  | Delete a book             |

## Example request

```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Clean Code","author":"Robert C. Martin","isbn":"9780132350884","publishedYear":2008}'
```

## Security notes (for the audit)

- HTTP Basic is used for demo purposes only; in production it should be replaced by
  a JWT resource server (`spring-boot-starter-oauth2-resource-server`).
- CSRF is disabled because this is a stateless REST API (no browser session),
  with the explicit exception of `/h2-console/**`.
- The H2 console and Swagger should be disabled or restricted in a real production
  deployment; here they are deliberately left open to ease development
  and auditing.
- Passwords are stored with `BCryptPasswordEncoder`.
- Generic error messages (`Exception.class`) never expose stack traces or
  internal details to the client.
