# applications

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Overview

This application is a **RESTful API service** for managing applications within a **Central Authorization System (Centrauthz)**. It serves as a microservice that provides CRUD operations for application metadata, supporting authorization and access control workflows in larger systems.

### Purpose and Functionality
- **Central Authorization**: Acts as a registry for applications, storing metadata such as names, descriptions, owners, and management groups.
- **Microservice Architecture**: Designed to integrate with other services for user authentication, permission checks, and application-based access control.
- **Key Role**: Enables other systems to query, create, update, or delete application records to enforce authorization policies.

### Technologies
- **Framework**: Quarkus (reactive, fast startup, native compilation support)
- **Database**: PostgreSQL with Flyway for schema migrations
- **Build Tool**: Gradle
- **Containerization**: Docker (multiple variants: JVM, native, legacy JAR)
- **API Documentation**: OpenAPI 3.1.0 (Swagger UI available in dev mode)

## Features

- **CRUD Operations**: Create, read, update, and delete applications via REST API.
- **Pagination and Filtering**: List applications with support for pagination (pageSize, pageToken) and filters (ownerId, managementGroupId, name).
- **Error Handling**: Global exception handler with standardized error responses (BadRequest, NotFound, Conflict, etc.).
- **Validation**: Hibernate Validator for request validation with configurable constraints.
- **Authentication Integration**: Uses `X-Auth-Request-User-Id` header for user context in operations.
- **Database Persistence**: JPA/Hibernate ORM with PostgreSQL, including audit fields (createdAt, updatedAt, createdBy, updatedBy).
- **Testing**: Comprehensive unit and integration tests for controllers, services, and error handling.
- **DevOps Support**: Docker images, native executables, and CI/CD ready with Gradle tasks.

## API Endpoints

The API is documented via OpenAPI and available at `/q/swagger-ui` in dev mode. Key endpoints:

- `POST /v0/applications`: Create a new application.
- `GET /v0/applications`: List applications with optional filters and pagination.
- `GET /v0/applications/{applicationKey}`: Retrieve a specific application.
- `PUT /v0/applications/{applicationKey}`: Update an application (partial update via patcher).
- `DELETE /v0/applications/{applicationKey}`: Delete an application.

All endpoints support the `X-Auth-Request-User-Id` header for authorization context.

## Database Schema

The application uses a single `application` table in PostgreSQL:

- `application_key` (VARCHAR, PK): Unique identifier for the application.
- `name` (VARCHAR): Human-readable name.
- `description` (TEXT): Optional description.
- `owner_id` (UUID): Owner of the application.
- `management_group_id` (UUID): Group with management permissions.
- `created_at` (TIMESTAMP): Creation timestamp.
- `created_by` (UUID): User who created the record.
- `updated_at` (TIMESTAMP): Last update timestamp.
- `updated_by` (UUID): User who last updated the record.

Indexes are created on `owner_id` and `management_group_id` for query performance. Schema is managed via Flyway migrations.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.native.enabled=true
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/applications-0.0.1-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/gradle-tooling>.

## Related Guides

- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
