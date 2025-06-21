# HR System Microservices Project

## Overview

This is a Microservices-based HR System built using **Spring Boot**, **Spring Cloud**, **Eureka**, **Spring Cloud Gateway**, **Kafka**, and **Docker Compose**. The system includes services for:

- User Authentication
- Job Posting
- Resume Parsing
- Application Submission
- Monitoring with Spring Boot Admin
- Asynchronous Resume Parsing triggered by Job Applications

---

## Project Structure

```
HrSystem/
├── api-gateway/
├── application-submit-service/
├── auth-service/
├── eureka-server/
├── job-posting-service/
├── resume-parser-service/
├── spring-boot-admin/
├── docker-compose.yml
└── .gitignore
```

---

## Services Description

| Service                        | Port | Description                                          |
| ------------------------------ | ---- | ---------------------------------------------------- |
| **Eureka Server**              | 8761 | Service discovery                                    |
| **API Gateway**                | 8080 | Gateway for routing requests                         |
| **Spring Boot Admin**          | 9090 | Admin UI to monitor microservices                    |
| **Auth Service**               | 8084 | Handles JWT-based authentication                     |
| **Job Posting Service**        | 8081 | Manages job postings                                 |
| **Resume Parser Service**      | 8083 | Parses resumes from uploaded PDF                     |
| **Application Submit Service** | 8082 | Handles job applications and triggers resume parsing |

---

## Docker Compose

Kafka and Zookeeper are used for asynchronous communication and are managed via `docker-compose.yml`:

```
docker-compose up
```

This command brings up:

- **Kafka**
- **Zookeeper**

---

## Security

- JWT-based authentication via `auth-service`
- HTTP-only cookie-based token handling through API Gateway

---

## Monitoring

- **Spring Boot Admin** monitors all microservices
- Integrated with **Spring Boot Actuator** for health and metrics

---

## Service Discovery

- All services register with **Eureka Server** at `http://localhost:8761`
- API Gateway uses Eureka for dynamic routing

---

## API Gateway Routing

| Path Prefix           | Mapped Service               | Final Endpoint Pattern       |
| --------------------- | ---------------------------- | ---------------------------- |
| `/jobs`               | `job-posting-service`        | `/api/jobs/**`               |
| `/applications`       | `application-submit-service` | `/api/applications/**`       |
| `/auth`               | `auth-service`               | `/api/auth/**`               |
| `/admin/users`        | `auth-service`               | `/api/admin/users/**`        |
| `/admin/jobs`         | `job-posting-service`        | `/api/admin/jobs/**`         |
| `/admin/applications` | `application-submit-service` | `/api/admin/applications/**` |

---

## Controller Endpoints

### 🔐 AuthController – `/api/auth`

| Method | Path        | Description                    |
| ------ | ----------- | ------------------------------ |
| POST   | `/login`    | Login with credentials         |
| POST   | `/register` | Register a new user            |
| POST   | `/logout`   | Logout user                    |
| GET    | `/me`       | Get current authenticated user |

### 👤 AdminController (Auth) – `/api/admin/users`

| Method | Path                        | Description                        |
| ------ | --------------------------- | ---------------------------------- |
| GET    | `/`                         | Get all users                      |
| GET    | `/{id}`                     | Get user by ID                     |
| PUT    | `/{id}`                     | Update user                        |
| PATCH  | `/{id}/enable?enabled=true` | Enable/disable user account        |
| GET    | `/user-summary`             | Get summary statistics about users |

### 🧾 JobController – `/api/jobs`

**For Users (ROLE\_USER)**

| Method | Path    | Description    |
| ------ | ------- | -------------- |
| GET    | `/`     | View all jobs  |
| GET    | `/{id}` | View job by ID |

**For HRs (ROLE\_HR)**

| Method | Path       | Description                   |
| ------ | ---------- | ----------------------------- |
| POST   | `/hr`      | Create a new job posting      |
| GET    | `/hr`      | Get all jobs posted by HR     |
| GET    | `/hr/{id}` | Get specific job posted by HR |
| PUT    | `/hr/{id}` | Update job by HR              |
| DELETE | `/hr/{id}` | Delete job by HR              |

### 🧾 AdminController (Jobs) – `/api/admin/jobs`

| Method | Path                | Description                  |
| ------ | ------------------- | ---------------------------- |
| GET    | `/`                 | Get all jobs                 |
| GET    | `/{id}`             | Get job by ID                |
| PUT    | `/{id}`             | Update any job               |
| DELETE | `/{id}`             | Delete job                   |
| GET    | `/count`            | Total job count              |
| GET    | `/count-by-company` | Job count grouped by company |

### 📩 ApplicationController – `/api/applications`

**For Users (ROLE\_USER)**

| Method | Path       | Description                           |
| ------ | ---------- | ------------------------------------- |
| POST   | `/submit`  | Submit application with resume upload |
| GET    | `/my`      | Get own applications                  |
| GET    | `/my/{id}` | Get specific application by ID        |
| PUT    | `/{id}`    | Update phone or resume                |

**For HRs (ROLE\_HR)**

| Method | Path                     | Description                         |
| ------ | ------------------------ | ----------------------------------- |
| GET    | `/hr`                    | Get applications for HR's job posts |
| GET    | `/hr/{id}`               | Get specific application by ID      |
| PATCH  | `/hr/{id}/status`        | Update application status           |
| GET    | `/hr/{id}/parsed-resume` | Get parsed resume for application   |

### 📂 AdminController (Applications) – `/api/admin/applications`

| Method | Path     | Description                 |
| ------ | -------- | --------------------------- |
| GET    | `/`      | Get all applications        |
| GET    | `/{id}`  | Get application by ID       |
| GET    | `/count` | Get total application count |

### 📄 ParsedResumeController – `/api/applications/parsed-resumes`

| Method | Path    | Description             |
| ------ | ------- | ----------------------- |
| GET    | `/`     | Get all parsed resumes  |
| GET    | `/{id}` | Get parsed resume by ID |

---

## Communication

- **Kafka** is used for event-driven scenarios:
   - When a user submits a job application, an event is published to Kafka
   - The **Resume Parser Service** listens for this event and automatically processes the uploaded resume

---

## Running the Project

### Prerequisites

- Java 17+
- Maven
- Docker
- IntelliJ IDEA or similar IDE

### Steps

1. Start Kafka and Zookeeper:
   ```bash
   docker-compose up
   ```
2. Start Eureka Server
3. Start Spring Boot Admin
4. Start API Gateway
5. Start remaining services (Auth, Job, Resume, Application)

### Access URLs

- Eureka Dashboard: `http://localhost:8761`
- Spring Boot Admin: `http://localhost:9090`
- API Gateway: `http://localhost:8080`

---

## Configuration Tips

Eureka client config for each service:

```yaml
eureka:
   client:
      service-url:
         defaultZone: http://localhost:8761/eureka
```

Spring Boot Admin client config:

```yaml
spring:
   boot:
      admin:
         client:
            url: http://localhost:9090
            instance:
               prefer-ip: true
```

---

## Future Enhancements

- Deploy to Kubernetes
- Build front-end using React or Thymeleaf
- Integrate OAuth2 or Keycloak for authentication

