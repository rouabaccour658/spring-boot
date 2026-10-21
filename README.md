# AutoLoc — Vehicle Rental Management System

## Overview

AutoLoc is a multi-agency vehicle rental management application developed with Java and Spring Boot. The project follows a layered architecture to organize business entities, data persistence, and business logic, using MySQL as the relational database.

The application is being developed incrementally, with a focus on maintainability, data consistency, and the implementation of RESTful services.

## Technology Stack

- Java 17
- Spring Boot 3.5.6
- Spring Web and Spring Data JPA
- MySQL
- Maven
- Lombok
- Jakarta Validation
- JUnit 5 and Spring Boot Test

## Main Features

- Domain model for vehicle rental management.
- Relational entity mapping using JPA.
- Data access through Spring Data JPA repositories.
- Business services for selected entities.
- Initial integration and service-level tests.

## Project Status

The project is under active development. The current implementation includes the domain model, data access layer, and initial business services. The next development stage focuses on expanding the business services, exposing application functionality through REST controllers, testing endpoints with Postman, and documenting the API with SpringDoc and Swagger UI.

## Architecture

The application is organized into layers:

- `domain`: business entities and enumerations.
- `repository`: database access interfaces.
- `service`: business service contracts.
- `service.impl`: business service implementations.
- `exception`: business exceptions.
- `web.controller`: REST API controllers.
- `web.dto`: data transfer objects, planned for a subsequent stage.

## Getting Started

The project requires Java 17, Maven, and a running MySQL instance. Database connection settings are configured in `src/main/resources/application.properties`.

To run the tests:

```bash
mvn clean test
```

To start the application:

```bash
mvn spring-boot:run
```

To package the application:

```bash
mvn clean package
```

Database credentials and local configuration should be adapted to the development environment. The default configuration is intended for local development, not production deployment.