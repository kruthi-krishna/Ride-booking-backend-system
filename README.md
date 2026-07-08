#  Ride-Sharing Backend System

A backend application built using Java Spring Boot that simulates the core functionalities of a ride-sharing platform like Uber or Ola. The system provides RESTful APIs for user and driver management, ride booking, ride tracking, fare calculation, and trip management while following a clean layered architecture.

---

Features

 User Module
- User Registration
- User Login
- Update User Profile
- Book a Ride
- Cancel Ride
- View Ride History

 Driver Module
- Driver Registration
- Driver Login
- Update Availability Status
- Accept/Reject Ride Requests
- Complete Rides
- View Assigned Trips

 Ride Management
- Book a Ride
- Automatic Driver Assignment
- Fare Calculation
- Ride Status Tracking
- Trip History

 Admin Module
- View All Users
- View All Drivers
- View All Rides
- Manage Ride Information

 Additional Features
- RESTful API Design
- Global Exception Handling
- Input Validation
- Layered Architecture
- Spring Data JPA
- MySQL Database Integration
- Swagger/OpenAPI Documentation
- Unit Testing
- Postman API Collection

---

Tech Stack

| Technology | Description |
|------------|-------------|
| Java 17 | Programming Language |
| Spring Boot | Backend Framework |
| Spring Data JPA | ORM |
| Hibernate | Database ORM |
| MySQL | Database |
| Maven | Build Tool |
| Spring Validation | Input Validation |
| Swagger/OpenAPI | API Documentation |
| JUnit 5 | Unit Testing |
| Postman | API Testing |
| Git & GitHub | Version Control |

---

 Project Architecture

```
RideSharingBackend
│
├── controller
├── service
├── repository
├── entity
├── dto
├── config
├── exception
├── resources
│    └── application.properties
│
├── pom.xml
└── README.md
```

The application follows a layered architecture to separate business logic, persistence, and presentation layers.

---

Database Design

 User

| Field |
|------|
| id |
| name |
| email |
| password |
| phone |

---

Driver

| Field |
|------|
| id |
| name |
| email |
| password |
| phone |
| vehicleNumber |
| vehicleType |
| available |

---

 Ride

| Field |
|------|
| id |
| pickupLocation |
| destination |
| distance |
| fare |
| status |
| bookingTime |
| userId |
| driverId |

---

Ride Workflow

```
User Login
      │
      ▼
Book Ride
      │
      ▼
Available Driver Assigned
      │
      ▼
Driver Accepts Ride
      │
      ▼
Ride Starts
      │
      ▼
Ride Completed
      │
      ▼
Fare Generated
```

---

API Endpoints

## User APIs

| Method | Endpoint |
|---------|----------|
| POST | /users/register |
| POST | /users/login |
| GET | /users/profile |
| PUT | /users/profile |
| GET | /users/rides |

---

Driver APIs

| Method | Endpoint |
|---------|----------|
| POST | /drivers/register |
| POST | /drivers/login |
| PUT | /drivers/status |
| GET | /drivers/rides |

---

Ride APIs

| Method | Endpoint |
|---------|----------|
| POST | /rides/book |
| PUT | /rides/{id}/cancel |
| PUT | /rides/{id}/accept |
| PUT | /rides/{id}/complete |
| GET | /rides/history |

---

Admin APIs

| Method | Endpoint |
|---------|----------|
| GET | /admin/users |
| GET | /admin/drivers |
| GET | /admin/rides |

---

Getting Started

Clone the Repository

```bash
git clone https://github.com/kruthi-krishna/RideSharingBackend.git
```

---

Configure MySQL

Update `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ridesharing
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
```

---

Build the Project

```bash
mvn clean install
```

---

Run the Application

```bash
mvn spring-boot:run
```

---

API Documentation

After starting the application, Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

Testing

The project includes unit tests for the service layer.

Run tests using:

```bash
mvn test
```

---

Future Enhancements

- JWT Authentication
- Real-Time Ride Tracking
- Payment Gateway Integration
- Driver Ratings & Reviews
- Notifications
- Docker Deployment
- Cloud Deployment (AWS)

---

Author

Kruthi Krishna

- GitHub: https://github.com/kruthi-krishna
  

