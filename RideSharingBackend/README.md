# 🚖 Ride-Sharing Backend System

A production-style backend system for a ride-booking platform (inspired by Uber/Ola, simplified for
a fresher-level portfolio project), built with **Java 17**, **Spring Boot 3**, **Spring Data JPA /
Hibernate**, and **MySQL**.

The system supports user & driver registration, automatic driver allocation, distance-based fare
calculation, ride lifecycle management, and admin oversight — all exposed as documented REST APIs.

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Database Schema](#-database-schema)
- [API Reference](#-api-reference)
- [Ride Lifecycle](#-ride-lifecycle)
- [Fare Calculation](#-fare-calculation)
- [Validation & Exception Handling](#-validation--exception-handling)
- [Getting Started](#-getting-started)
- [Testing the APIs](#-testing-the-apis)
- [Running Unit Tests](#-running-unit-tests)
- [Screenshots](#-screenshots)
- [Future Enhancements](#-future-enhancements)
- [Resume Bullet Mapping](#-resume-bullet-mapping)

---

## ✨ Features

- 👤 **User module** — registration, login, profile view/update, ride history
- 🚗 **Driver module** — registration, login, availability toggling, ride history
- 🧭 **Ride booking** — with **automatic driver allocation** (first available driver)
- 💰 **Distance-based fare calculation**
- 🔄 **Ride lifecycle management** — `REQUESTED → ACCEPTED → ONGOING → COMPLETED / CANCELLED`
- 🛡️ **Global exception handling** with consistent JSON error responses
- ✅ **Request validation** using Bean Validation annotations
- 📖 **Swagger / OpenAPI** documentation, interactive at `/swagger-ui.html`
- 🗄️ **Layered architecture** (Controller → Service → ServiceImpl → Repository → Entity)
- 🧪 **Unit tests** with JUnit 5 & Mockito

---

## 🛠 Tech Stack

| Category            | Technology                              |
|---------------------|------------------------------------------|
| Language             | Java 17                                  |
| Framework            | Spring Boot 3.3                          |
| Persistence          | Spring Data JPA, Hibernate                |
| Database             | MySQL 8                                  |
| Build Tool           | Maven                                    |
| Boilerplate          | Lombok                                   |
| Validation           | Jakarta Bean Validation                  |
| Security             | Spring Security (password hashing; auth pluggable — see Future Enhancements) |
| API Docs             | springdoc-openapi (Swagger UI)            |
| Testing              | JUnit 5, Mockito                          |
| Version Control      | Git / GitHub                             |

---

## 🏗 Architecture

Classic layered (N-tier) architecture, the same pattern used in many production Spring Boot codebases:

```
Client (Postman / Swagger UI)
        │
        ▼
  Controller Layer      →  REST endpoints, request/response mapping
        │
        ▼
  Service Layer          →  Interfaces defining business contracts
        │
        ▼
  ServiceImpl Layer      →  Business logic, transactions, orchestration
        │
        ▼
  Repository Layer       →  Spring Data JPA interfaces
        │
        ▼
  Entity Layer            →  JPA-mapped MySQL tables
```

### Project Structure

```
RideSharingBackend
├── src/main/java/com/ridesharing/backend
│   ├── config           → SecurityConfig, OpenApiConfig
│   ├── controller        → UserController, DriverController, RideController, AdminController
│   ├── dto               → Request/response DTOs (validated)
│   ├── entity             → User, Driver, Ride, RideStatus
│   ├── exception          → Custom exceptions + GlobalExceptionHandler
│   ├── repository         → UserRepository, DriverRepository, RideRepository
│   ├── service            → Service interfaces
│   ├── serviceImpl        → Service implementations (business logic)
│   ├── util               → FareCalculator, EntityMapper
│   └── RideSharingApplication.java
├── src/main/resources
│   └── application.properties
├── src/test/java          → Unit tests (JUnit + Mockito)
├── pom.xml
└── README.md
```

---

## 🗄 Database Schema

**User**

| Column     | Type          | Notes            |
|------------|---------------|------------------|
| id          | BIGINT (PK)   | auto-increment    |
| name        | VARCHAR       | not null          |
| email       | VARCHAR       | unique, not null  |
| password    | VARCHAR       | BCrypt-hashed     |
| phone       | VARCHAR       | 10-digit          |
| createdAt   | TIMESTAMP     | set on creation   |

**Driver**

| Column        | Type          | Notes             |
|---------------|---------------|--------------------|
| id             | BIGINT (PK)   | auto-increment      |
| name           | VARCHAR       | not null            |
| email          | VARCHAR       | unique, not null    |
| password       | VARCHAR       | BCrypt-hashed       |
| phone          | VARCHAR       | 10-digit            |
| vehicleNumber  | VARCHAR       | not null            |
| vehicleType    | VARCHAR       | not null            |
| available      | BOOLEAN       | default true        |
| rating         | DOUBLE        | default 5.0         |

**Ride**

| Column        | Type          | Notes                                   |
|---------------|---------------|-------------------------------------------|
| id             | BIGINT (PK)   | auto-increment                             |
| pickup         | VARCHAR       | not null                                   |
| destination    | VARCHAR       | not null                                   |
| distance       | DOUBLE        | km, > 0                                    |
| fare           | DOUBLE        | computed                                   |
| status         | ENUM          | REQUESTED/ACCEPTED/ONGOING/COMPLETED/CANCELLED |
| rideTime       | TIMESTAMP     | set on creation                            |
| user_id        | BIGINT (FK)   | → users.id                                 |
| driver_id      | BIGINT (FK)   | → drivers.id, nullable                     |

Relationships: `User (1) ── (N) Ride`, `Driver (1) ── (N) Ride`.

---

## 📡 API Reference

Base URL: `http://localhost:8080`

### User APIs

| Method | Endpoint                 | Description                  |
|--------|---------------------------|-------------------------------|
| POST   | `/users/register`          | Register a new user           |
| POST   | `/users/login`              | User login                    |
| GET    | `/users/profile/{id}`       | Get user profile              |
| PUT    | `/users/profile/{id}`       | Update user profile           |
| GET    | `/users/{id}/rides`         | Get a user's ride history     |

### Driver APIs

| Method | Endpoint                  | Description                       |
|--------|----------------------------|-------------------------------------|
| POST   | `/drivers/register`          | Register a new driver               |
| POST   | `/drivers/login`              | Driver login                        |
| PUT    | `/drivers/{id}/status`        | Update driver availability status   |
| GET    | `/drivers/{id}/rides`         | Get a driver's ride history         |

### Ride APIs

| Method | Endpoint                | Description                                              |
|--------|---------------------------|-------------------------------------------------------------|
| POST   | `/rides/book`               | Book a ride (auto-assigns first available driver, if any)    |
| PUT    | `/rides/{id}/accept?driverId=` | Driver explicitly accepts a requested ride                |
| PUT    | `/rides/{id}/complete`       | Mark a ride as completed                                     |
| PUT    | `/rides/{id}/cancel`         | Cancel a ride                                                 |
| GET    | `/rides/{id}`                | Get ride details by id                                       |
| GET    | `/rides/history`             | Get complete ride history (all rides)                        |

### Admin APIs

| Method | Endpoint             | Description             |
|--------|------------------------|---------------------------|
| GET    | `/admin/users`          | List all users            |
| GET    | `/admin/drivers`        | List all drivers          |
| GET    | `/admin/rides`          | List all rides            |
| DELETE | `/admin/user/{id}`      | Delete a user             |

Every endpoint returns a consistent envelope:

```json
{
  "success": true,
  "message": "Ride booked successfully",
  "data": { "...": "..." }
}
```

---

## 🔄 Ride Lifecycle

```
REQUESTED ──(driver accepts)──▶ ACCEPTED ──▶ ONGOING ──▶ COMPLETED
     │                              │
     └───────────(cancel)───────────┴──────────────────▶ CANCELLED
```

- On booking, the system automatically looks for the **first available driver**
  (`findFirstByAvailableTrue`) and assigns them, moving the ride straight to `ACCEPTED`.
- If no driver is free, the ride stays `REQUESTED` until a driver calls `/rides/{id}/accept`.
- Completing or cancelling a ride frees up the assigned driver (`available = true`) again.

---

## 💰 Fare Calculation

```
Base Fare = ₹50
Per KM     = ₹15

fare = 50 + (distance_in_km × 15)
```

Implemented in `util/FareCalculator.java` and covered by unit tests in
`FareCalculatorTest.java`.

---

## ✅ Validation & Exception Handling

**Bean Validation annotations** used across DTOs: `@NotBlank`, `@Email`, `@Pattern`, `@NotNull`, `@Positive`.

**Custom exceptions**, all handled centrally by `GlobalExceptionHandler` (`@RestControllerAdvice`):

| Exception                     | HTTP Status |
|--------------------------------|-------------|
| `UserNotFoundException`         | 404         |
| `DriverNotFoundException`       | 404         |
| `RideNotFoundException`         | 404         |
| `EmailAlreadyExistsException`   | 409         |
| `InvalidRideException`          | 400         |
| `InvalidCredentialsException`   | 401         |
| `MethodArgumentNotValidException` (validation failures) | 400 |

All error responses share one shape:

```json
{
  "timestamp": "2026-07-08 10:15:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 42",
  "path": "/users/profile/42",
  "validationErrors": null
}
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8 running locally (or update the connection string)

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/RideSharingBackend.git
cd RideSharingBackend
```

### 2. Configure MySQL

Create nothing manually — the app auto-creates the schema — but do update credentials in
`src/main/resources/application.properties` if yours differ from the defaults:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ride_sharing_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Build & run

```bash
mvn clean install
mvn spring-boot:run
```

The application starts on **http://localhost:8080**.

### 4. Explore the API docs

Open:

```
http://localhost:8080/swagger-ui.html
```

to see and try every endpoint interactively.

---

## 🧪 Testing the APIs

A ready-to-import Postman collection is recommended (`postman_collection.json`, add your own export),
or use the Swagger UI directly. Typical flow:

1. `POST /users/register` → register a rider
2. `POST /drivers/register` → register a driver, then `PUT /drivers/{id}/status` to mark `available: true`
3. `POST /rides/book` → book a ride for the user (auto-assigns the driver)
4. `PUT /rides/{id}/complete` → complete the ride
5. `GET /rides/history` → confirm the ride shows as `COMPLETED`

---

## 🧪 Running Unit Tests

```bash
mvn test
```

Includes:
- `FareCalculatorTest` — validates the fare formula across edge cases
- `RideServiceImplTest` (JUnit 5 + Mockito) — validates booking, automatic driver allocation, and
  not-found handling using mocked repositories

---

## 📸 Screenshots

> Add screenshots here once the app is running locally, e.g.:
> - Swagger UI overview
> - Successful ride booking response
> - MySQL table view of `rides`

```
/screenshots
  ├── swagger-ui.png
  ├── book-ride-response.png
  └── mysql-rides-table.png
```

---

## 🔮 Future Enhancements

- 🔐 **JWT authentication** — replace the current permit-all `SecurityConfig` with a stateless JWT
  filter chain and role-based access (`USER`, `DRIVER`, `ADMIN`)
- 🐳 **Docker** — containerize the app and MySQL via `docker-compose.yml`
- 🗺️ **Real distance calculation** via a maps/geocoding API instead of client-supplied distance
- 📍 **Live driver location tracking** (WebSockets)
- 💳 **Payment integration**
- 📊 **Pagination & filtering** on admin/history endpoints
- 🧾 **Rate limiting** and structured logging (SLF4J + ELK)

---

## 📝 Resume Bullet Mapping

Every bullet below is backed by actual code in this repository:

- **Designed and developed a scalable backend system supporting ride booking, driver allocation,
  trip management, and fare calculation.** → `RideServiceImpl`, `FareCalculator`, layered architecture
- **Built RESTful APIs for user registration, authentication, ride requests, trip tracking, and
  driver management.** → `UserController`, `DriverController`, `RideController`, `AdminController`
- **Designed relational database schemas and optimized SQL queries for efficient ride and user data
  management.** → `entity` package + JPA repository query methods (`findFirstByAvailableTrue`,
  `findByUserId`, `findByDriverId`)
- **Added input validation, exception handling, and backend testing to improve application
  reliability.** → Bean Validation on DTOs, `GlobalExceptionHandler`, JUnit/Mockito tests
- **Used Git for version control and collaborative development.** → see suggested commit history below

### Suggested Commit History

```
git init
git add pom.xml
git commit -m "Initial Spring Boot setup"

git add src/main/java/com/ridesharing/backend/entity src/main/java/com/ridesharing/backend/repository
git commit -m "Added User and Driver entity + repository layer"

git add src/main/java/com/ridesharing/backend/dto
git commit -m "Added request/response DTOs with validation"

git add src/main/java/com/ridesharing/backend/serviceImpl src/main/java/com/ridesharing/backend/service
git commit -m "Implemented User, Driver, and Ride service layer"

git add src/main/java/com/ridesharing/backend/controller
git commit -m "Created REST APIs for user, driver, ride, and admin modules"

git add src/main/java/com/ridesharing/backend/exception
git commit -m "Added centralized exception handling"

git add src/main/java/com/ridesharing/backend/config
git commit -m "Integrated Swagger/OpenAPI and Spring Security password hashing"

git add src/test
git commit -m "Added unit tests with JUnit and Mockito"

git add README.md .gitignore
git commit -m "Updated README with docs, schema, and setup guide"
```

---

## 📄 License

This project is provided for educational/portfolio purposes.
