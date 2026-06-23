# Inventory Management System

This project is organized into two main directories:

## Project Structure

```
inventory-management/
├── backend/          # Spring Boot backend application
├── frontend/         # Frontend application (to be implemented)
└── README.md         # This file
```

### Backend

The `backend/` directory contains the Spring Boot REST API application for inventory management.

- **Location**: `./backend/`
- **Technology**: Java, Spring Boot
- **Build Tool**: Maven
- **Getting Started**: 
  ```bash
  cd backend
  ./mvnw clean install
  ./mvnw spring-boot:run
  ```

### Frontend

The `frontend/` directory is reserved for the frontend application.

- **Location**: `./frontend/`
- **Suggested Technologies**: React, Angular, Vue.js, or your preferred framework
- **Setup**: Create your frontend project structure here

## Development Workflow

1. **Backend Development**: Work in the `backend/` directory for API changes
2. **Frontend Development**: Work in the `frontend/` directory for UI/UX changes
3. **Git Management**: Both directories are tracked in a single repository

## Notes

- Each directory can maintain its own dependencies and build configuration
- Ensure the frontend correctly points to the backend API endpoints
- Update `.gitignore` as needed for frontend dependencies and build outputs
