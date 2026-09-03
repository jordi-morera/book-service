# Book Service

Microservicio base construido con **Java 25** y **Spring Boot 3.5**, pensado como
punto de partida limpio y con buenas practicas para ser auditado posteriormente
por agentes de IA (seguridad, calidad, arquitectura).

## Stack

- Java 25 (Records, Text Blocks, Pattern Matching donde aplica)
- Spring Boot 3.5 (Web, Data JPA, Security 6, Validation, Actuator)
- H2 en memoria
- springdoc-openapi (Swagger UI)
- Maven

## Arquitectura

```
Controller -> Service (interfaz + impl) -> Repository -> Entity
DTOs (Record) para request/response, desacoplados de la entidad JPA
GlobalExceptionHandler (@RestControllerAdvice) para errores uniformes
SecurityConfig (SecurityFilterChain) para autenticacion/autorizacion
```

## Como levantar el proyecto

Requisitos: JDK 25 y Maven (o usa el wrapper `./mvnw` si lo generas con `mvn -N wrapper:wrapper`).

```bash
mvn spring-boot:run
```

La aplicacion arranca en `http://localhost:8080`.

## Credenciales de demo (HTTP Basic)

- Usuario: `admin`
- Password: `admin123`

Todos los endpoints bajo `/api/v1/books/**` requieren autenticacion.
El endpoint `GET /api/v1/public/status` es publico (sin autenticacion), a modo de ejemplo.

## Swagger / OpenAPI

- UI: http://localhost:8080/swagger-ui.html
- JSON del contrato: http://localhost:8080/v3/api-docs

## Consola H2

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:bookdb`
- Usuario: `sa`
- Password: *(vacio)*

## Endpoints principales

| Metodo | Ruta                     | Auth   | Descripcion              |
|--------|--------------------------|--------|---------------------------|
| GET    | /api/v1/public/status    | No     | Health check publico      |
| GET    | /api/v1/books            | Basic  | Lista todos los libros    |
| GET    | /api/v1/books/{id}       | Basic  | Obtiene un libro          |
| POST   | /api/v1/books            | Basic  | Crea un libro             |
| PUT    | /api/v1/books/{id}       | Basic  | Actualiza un libro        |
| DELETE | /api/v1/books/{id}       | Basic  | Elimina un libro          |

## Ejemplo de request

```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Clean Code","author":"Robert C. Martin","isbn":"9780132350884","publishedYear":2008}'
```

## Notas de seguridad (para la auditoria)

- HTTP Basic se usa solo como demo; en produccion se recomienda sustituirlo por
  un resource server JWT (`spring-boot-starter-oauth2-resource-server`).
- CSRF esta deshabilitado por tratarse de una API REST stateless (sin sesion de
  navegador), salvo la excepcion explicita de `/h2-console/**`.
- La consola H2 y Swagger deben deshabilitarse o restringirse en un despliegue
  productivo real; aqui quedan abiertos deliberadamente para facilitar el desarrollo
  y la auditoria.
- Las contrasenas se almacenan con `BCryptPasswordEncoder`.
- Los mensajes de error genericos (`Exception.class`) no exponen stacktraces ni
  detalles internos al cliente.
