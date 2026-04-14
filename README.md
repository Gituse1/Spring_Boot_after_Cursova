# EasyBus - Bus Ticket Booking System

REST API backend for a bus ticket booking platform.  
This is a training project developed after the coursework to practice and demonstrate real-world Spring Boot skills.

## Technologies Used

- **Java 17**
- **Spring Boot 3** (Spring Web, Spring Data JPA, Spring Security)
- **MySQL** + **Hibernate**
- **Lombok**
- **MapStruct**
- **Jakarta Bean Validation** (including custom validators)
- **Global Exception Handling** (@ControllerAdvice)
- **JUnit 5 + Mockito** (Unit and Integration tests)

## Core Features

### User Role:
- Browse available routes and schedules
- Search for bus trips
- Book tickets for available seats
- View personal booking history

### Admin Role:
- Full CRUD management of routes
- Manage schedules/trips (create, update, delete)
- Basic user management

## Architecture

The project follows a clean **multi-layer architecture**:
**Controller → Service → Repository**

- DTO pattern for all API requests/responses
- Role-based access control with Spring Security
- MapStruct for mapping between entities and DTOs
- Global exception handling

### Project Structure
```text
src/main/java/com/example/
├── controller/          # User & Admin REST controllers
├── service/             # Business logic
├── repository/          # Spring Data JPA repositories
├── model/               # JPA Entities (Route, Trip, Booking, etc.)
├── dto/                 # Data Transfer Objects
├── mapper/              # MapStruct mappers
├── config/              # Security and app configuration
├── exception/           # Global exception handler

```
How to Run

Clone the repository
Create a local MySQL database (e.g. easybus_db)
Update src/main/resources/application.properties with your database credentials
Run the application:Bashmvn spring-boot:run

API will be available at http://localhost:8080
Testing
The project contains unit tests and integration tests in the src/test directory.
Status

Portfolio / Training Project

Created to demonstrate practical experience with Spring Boot ecosystem, relational databases, REST API development, validation, security, and testing.
