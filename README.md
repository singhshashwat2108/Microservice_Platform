# QueryHub — Microservices (Phase 1)

QueryHub is a query management platform migrated from a monolith into a microservice architecture.

## Architecture (Phase 1)

```text
Client (HTML/JS on View Service :8083)
        |
        +-- Auth Service (:8081) --> auth_db (MySQL)
        +-- Query Service (:8082) --> query_db (MySQL)
        +-- View Service (:8083) --> Query Service (read-only, no DB)
```

### Services

| Service | Port | Database | Responsibility |
|---------|------|----------|----------------|
| **auth-service** | 8081 | `auth_db` | Registration, login, JWT, profile |
| **query-service** | 8082 | `query_db` | Queries, categories, ownership rules |
| **view-service** | 8083 | None | Read-only views + static frontend |

## Project Structure

```text
QueryBoard_SpringBoot/
├── common-lib/          Shared JWT, CORS, error handling
├── auth-service/
├── query-service/
├── view-service/
├── docker-compose.yml
└── pom.xml              Maven parent (multi-module)
```

## API Endpoints

### Auth Service (`:8081`)

| Method | Path | Auth |
|--------|------|------|
| POST | `/auth/register` | Public |
| POST | `/auth/login` | Public |
| GET | `/auth/profile` | JWT |
| POST | `/auth/validate` | Public |

### Query Service (`:8082`)

| Method | Path | Auth |
|--------|------|------|
| POST | `/query` | JWT |
| GET | `/query/{id}` | Public |
| GET | `/query/all` | Public |
| GET | `/query/latest` | Public |
| PUT | `/query/{id}` | Owner |
| DELETE | `/query/{id}` | Owner |
| POST | `/category` | JWT |
| GET | `/category` | Public |

### View Service (`:8083`)

| Method | Path | Auth |
|--------|------|------|
| GET | `/view/query/{id}` | Public |
| GET | `/view/all` | Public |
| GET | `/view/latest` | Public |

Static frontend: `http://localhost:8083/index.html`

## Run Locally (without Docker)

### Prerequisites

- Java 17
- Maven 3.9+
- MySQL 8 with databases `auth_db` and `query_db`

```sql
CREATE DATABASE auth_db;
CREATE DATABASE query_db;
```

Update credentials in each service's `application.properties` if needed.

### Build

```bash
mvn clean package
```

### Start services (three terminals)

```bash
java -jar auth-service/target/auth-service-0.0.1-SNAPSHOT.jar
java -jar query-service/target/query-service-0.0.1-SNAPSHOT.jar
java -jar view-service/target/view-service-0.0.1-SNAPSHOT.jar
```

Open **http://localhost:8083**

## Run with Docker

```bash
docker compose up --build
```

Services:

- Frontend: http://localhost:8083
- Auth API: http://localhost:8081
- Query API: http://localhost:8082

## Phase Roadmap

| Phase | Features |
|-------|----------|
| **1 (current)** | Auth, Query, View services, separate DBs, Docker |
| 2 | API Gateway, Comments, Likes |
| 3 | Redis cache-aside, TTL, invalidation |
| 4 | Kafka, Notification Service |

## Tech Stack

- Java 17, Spring Boot 3.4
- Spring Security + JWT
- Spring Data JPA + MySQL
- Maven multi-module
- Docker Compose
