# Query Management System

A full-stack Query Management Platform built using Spring Boot, Spring Security, JWT Authentication, MySQL, JPA/Hibernate, and Thymeleaf.

The application allows users to register, authenticate, create and manage queries, categorize content, interact through comments and likes, and browse queries through a web interface.

---

## Features

### Authentication & Authorization

* User Registration
* User Login
* JWT-based Authentication
* Password Encryption using BCrypt
* Protected API Endpoints
* Stateless Security Configuration

### Query Management

* Create Query
* Update Query
* Delete Query (Owner Only)
* View Queries
* Filter Queries by Category

### Category Management

* Create Categories
* Organize Queries by Category

### Comments & Likes

* Add Comments to Queries
* Guest Comments with Optional Name
* Like Queries

### Frontend

* Homepage
* Login Page
* Registration Page
* Query Listing Page
* Query Creation Form
* Category-based Filtering

---

## Tech Stack

### Backend

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* JWT Authentication

### Database

* MySQL

### Frontend

* Thymeleaf
* HTML
* CSS
* JavaScript

### Build Tool

* Maven

---

## Project Architecture

The application follows a layered monolithic architecture.

```text
Controller Layer
        │
        ▼
Service Layer
        │
        ▼
Repository Layer
        │
        ▼
MySQL Database
```

### Main Components

```text
Controllers
├── AuthController
├── QueryController
├── CategoryController
├── CommentController

Services
├── AuthService
├── QueryService
├── CategoryService
├── CommentService

Repositories
├── UserRepository
├── QueryRepository
├── CategoryRepository
├── CommentRepository
```

---

## Security Flow

```text
User Login
     │
     ▼
AuthenticationManager
     │
     ▼
UserDetailsService
     │
     ▼
Database Validation
     │
     ▼
JWT Generation
     │
     ▼
JWT Returned To Client
```

Subsequent requests:

```text
Authorization: Bearer <JWT_TOKEN>
```

are validated using Spring Security filters before accessing protected endpoints.

---

## Database Design

### Users

* id
* username
* password
* role

### Categories

* id
* name

### Queries

* id
* title
* description
* category_id
* user_id
* created_at

### Comments

* id
* query_id
* guest_name
* content
* created_at

### Likes

* id
* query_id
* user_id

---