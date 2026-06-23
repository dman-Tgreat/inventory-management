# Backend - Inventory Management API

## Overview

This is a Spring Boot REST API application that provides endpoints for managing inventory, products, purchases, sales, and user authentication.

## Project Structure

```
backend/
├── pom.xml              # Maven configuration
├── mvnw                 # Maven wrapper (Unix)
├── mvnw.cmd             # Maven wrapper (Windows)
├── .mvn/                # Maven configuration directory
├── src/
│   ├── main/java/com/inventory/inventory_management/
│   │   ├── InventoryApplication.java
│   │   ├── config/              # Configuration classes
│   │   ├── controller/          # REST endpoints
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access
│   │   ├── entity/              # JPA entities
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── mapper/              # Entity to DTO mappers
│   │   ├── security/            # Security configuration
│   │   ├── exception/           # Custom exceptions
│   │   └── util/                # Utility classes
│   └── test/                    # Unit tests
└── target/              # Build output directory
```

## Features

- User authentication and authorization
- Product management
- Category management
- Supplier management
- Purchase and sales tracking
- Inventory management with logging
- Dashboard with summary information
- Swagger/OpenAPI documentation

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+

### Build

```bash
cd backend
./mvnw clean install
```

### Run

```bash
./mvnw spring-boot:run
```

The application will start at `http://localhost:8080`

### API Documentation

Swagger UI documentation is available at:
```
http://localhost:8080/swagger-ui.html
```

## Configuration

- Update `application.properties` or `application.yml` for database and other configurations
- Security settings are defined in `config/SecurityConfig.java`

## Testing

Run tests with:
```bash
./mvnw test
```

## Development

- Use the controller classes in the `controller/` directory to add new endpoints
- Create services in the `service/` directory for business logic
- Define JPA entities in the `entity/` directory
- Use DTOs for API request/response payloads

---

**Note**: The frontend application communicates with this API. Ensure CORS is properly configured if running on different ports/domains.
