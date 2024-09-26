# Tutor Booking Application - Backend

This project is the backend of the **Tutor Booking Application**, a web-based platform that allows users to book tutoring sessions. It is built with Spring Boot, provides RESTful APIs, and includes JWT-based authentication for role-based access control.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [JPA and Database Architecture](#jpa-and-database-architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Backend](#running-the-backend)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Logging](#logging)

## Features

- **User registration and login**: Role-based access control (Students and Tutors).
- **JWT Authentication**: Secures endpoints for authorized users.
- **Session management** (CRUD operations):
  - Tutors can create, update, and delete sessions.
  - Students can browse available sessions and enroll in them.
- **Logging**: Log4j for managing application logs.

## Technologies Used

- **Backend**: Java, Spring Boot, JPA, Hibernate, MySQL, Log4j.
- **Authentication**: JWT (JSON Web Token).
- **Build Tools**: Maven.
- **Version Control**: Git, GitHub.

## JPA and Database Architecture

This project utilizes **Java Persistence API (JPA)** to manage interactions with the database. We have two main entities: `User` and `Session`, which form the core structure of the system. These entities are linked through different relationships that reflect the nature of the application's business logic.

### Main Entities:

1. **User**: Represents individuals who can either be **students** or **tutors**.
    - Key fields: `username`, `password`, `email`, `role`.
    - Relationships:
        - **Many-to-Many** with `Session`: A user (student) can enroll in multiple sessions, and each session can have multiple students.
        - **One-to-Many** with `Session`: A user (tutor) can own many sessions.
        - **One-to-Many** with `Review`: Users can write and receive reviews for sessions, though not fully detailed here.

2. **Session**: Represents a class or course that students can enroll in and tutors can teach.
    - Key fields: `sessionName`, `startDate`, `endDate`, `frequency`, `duration`, `status`.
    - Relationships:
        - **Many-to-Many** with `User` (students): Multiple students can be enrolled in one session, and a student can be in multiple sessions.
        - **Many-to-One** with `User` (tutor): Each session is taught by a single tutor.

### JPA Annotations:

- **@Entity**: Marks the class as a JPA entity (mapped to a database table).
- **@Id** and **@GeneratedValue**: Primary key configuration for the entities.
- **@Column**: Defines specific attributes of database columns (e.g., `nullable`, `unique`).
- **@ManyToMany** and **@JoinTable**: Defines many-to-many relationships between students and sessions.
- **@OneToMany** and **@ManyToOne**: Handles relationships between tutors and sessions.
- **@JsonIgnore**: Used to avoid circular references when serializing to JSON.

### Database Schema:

1. **User Table**: Contains columns such as `userId`, `username`, `password`, `email`, and `role`.
2. **Session Table**: Contains columns such as `sessionId`, `sessionName`, `startDate`, `endDate`, `frequency`, `duration`, and a foreign key `FK_TUTOR_ID` linking to the tutor.
3. **Student_Session Join Table**: Links students to sessions via foreign keys `FK_STUDENT_ID` and `FK_SESSION_ID`.

This structure supports the core functionality of the system, allowing users to sign up for sessions, manage courses, and maintain relational integrity between users and classes. JPA handles all database interactions, ensuring consistency and simplifying database management.

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL** (or any other relational database)
- **Git**

### Installation

1. **Clone the repository**:

    ```bash
    git clone https://github.com/SusanLYC/TutorBookingApp_Backend.git
    cd tutor-booking-app
    ```

2. **Set up the backend**:

    - Navigate to the `backend` directory:

      ```bash
      cd backend
      ```

    - Configure your database in `src/main/resources/application.properties`:

      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/tutor_booking_db
      spring.datasource.username=your_db_username
      spring.datasource.password=your_db_password
      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.show-sql=true
      ```

    - Build the project with Maven:

      ```bash
      mvn clean install
      ```

### Running the Backend

1. **Start the backend**:

    ```bash
    mvn spring-boot:run
    ```

    The backend server will start on `http://localhost:8080`.

## API Endpoints

### User Management

- **POST /api/v1/users/signup**: Sign up a new user.
- **POST /api/v1/auth/login**: Log in with credentials and receive a JWT token.
- **GET /api/v1/users**: Get all users (requires authentication).
- **GET /api/v1/users/{id}**: Get user by ID (requires authentication).

### Session Management

- **GET /api/v1/session**: Get all available sessions.
- **POST /api/v1/session/createcourse?tutorId={tutorId}**: Create a session as a TUTOR.
- **POST /api/v1/users/{userId}/sessions/{sessionId}**: Enroll in a session as a STUDENT.
- **PUT /api/v1/session/{sessionId}/updatestatus**: Update session status as a TUTOR.
- **DELETE /api/v1/session/{sessionId}/delete**: Delete session as a TUTOR.

## Authentication

This project uses **JWT (JSON Web Token)** for securing the API endpoints.

- **JWT Generation**: On successful login, the server generates a JWT for the user.
- **Token Usage**: The token is sent in the `Authorization` header of subsequent requests to authenticate and authorize the user.
- **Protected Endpoints**: Only authorized users with valid JWT tokens can access certain endpoints (e.g., creating/deleting sessions, viewing user details).

### Example: Login Process

1. **POST /api/v1/auth/login**: Sends the user’s credentials (username and password).
   - On success, the server responds with a JWT token.
2. **Subsequent Requests**: The token is passed in the `Authorization: Bearer <token>` header for authenticated requests.

### Role-based Access Control

- **Students**: Can browse available sessions and enroll in them.
- **Tutors**: Can create, update, and delete sessions.

## Logging

This application uses **Log4j** for logging. By default, logs are written to the console. You can configure logging settings in the `log4j2.xml` file located in the `src/main/resources` directory.

### Example Log Statements

- **INFO**: General information about application flow (e.g., "Fetching all users").
- **WARN**: Warnings about potential issues (e.g., "User not found").
- **ERROR**: Errors and exceptions (e.g., "Failed to enroll user in session").
