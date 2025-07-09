# HR System Microservices Project

## Overview

This project is a microservices-based Human Resources (HR) System developed in Java using **Spring Boot**, **Spring Cloud**, **Eureka**, **Spring Cloud Gateway**, **Kafka**, and **Docker Compose**. It features secure authentication, job posting, automated LLM-powered resume parsing, and asynchronous event-driven workflows.  
**Ollama** is integrated for advanced resume analysis using the custom-instructed **IBM Granite 3.3:2b** model.

---

## Table of Contents

- [Project Structure](#project-structure)
- [Features](#features)
- [Architecture](#architecture)
- [Service Overview](#service-overview)
- [Kafka Messaging](#kafka-messaging)
- [Ollama LLM Integration](#ollama-llm-integration)
- [Deployment](#deployment)
- [Security](#security)
- [Monitoring](#monitoring)
- [API Gateway Routing](#api-gateway-routing)
- [Communication](#communication)
- [Customization](#customization)
- [License](#license)

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
├── ollama/                 # Ollama config & Modelfile for IBM Granite 3.3:2b
├── docker-kafka.yaml       # Kafka-only deployment 
├── docker-ollama.yaml      # Ollama LLM deployment
├── docker-compose.yaml     # Main stack orchestration (optional)
└── .gitignore
```

---

## Features

- **User Authentication** (JWT, HTTP-only cookies)
- **Job Posting and Management**
- **Resume Upload & Parsing** (LLM-powered extraction with IBM Granite 3.3:2b)
- **Application Submission**
- **Event-driven Microservices** (Kafka-based)
- **Service Discovery** (Eureka)
- **API Gateway** (Spring Cloud Gateway)
- **Monitoring** (Spring Boot Admin)
- **Containerized Deployment** (Docker Compose)

---

## Architecture

- **Backend:** Java (Spring Boot Microservices)
- **Messaging:** Kafka 
- **LLM Integration:** Ollama (IBM Granite 3.3:2b custom, via Modelfile)
- **Service Discovery:** Eureka
- **API Gateway:** Spring Cloud Gateway
- **Monitoring:** Spring Boot Admin + Actuator

---

## Service Overview

| Service                        | Port   | Description                                             |
| ------------------------------ | ------ | ------------------------------------------------------- |
| **Eureka Server**              | 8761   | Service discovery for all microservices                 |
| **API Gateway**                | 8080   | Entry point, routes requests to internal services       |
| **Spring Boot Admin**          | 9090   | Monitoring dashboard for all microservices              |
| **Auth Service**               | 8084   | User authentication, JWT issuing                        |
| **Job Posting Service**        | 8081   | CRUD for job postings                                   |
| **Resume Parser Service**      | 8083   | PDF extraction + LLM (Ollama) analysis                  |
| **Application Submit Service** | 8082   | Handles job applications, triggers resume parsing        |
| **Kafka**                      | 9092   | Event bus for async communication  |
| **Ollama**                     | 11434  | Local LLM API for resume analysis (IBM Granite 3.3:2b)  |

---

## Kafka Messaging 

Kafka is deployed using `docker-kafka.yaml`.  
This enables lightweight, reliable messaging for all async flows (e.g., when a job application is submitted, resume parsing is triggered via Kafka event).

**Start Kafka:**
```bash
docker compose -f docker-kafka.yaml up -d
```

---

## Ollama LLM Integration

- **Ollama** runs as a separate container (see `docker-ollama.yaml`), serving the **IBM Granite 3.3:2b** model, customized via the `ollama/Modelfile`.
- **Resume parsing** uses this LLM to extract and structure candidate data from PDFs with advanced, instructable prompts.

**Start Ollama:**
```bash
docker compose -f docker-ollama.yaml up -d
```

**Customizing the Model:**
- The `ollama/Modelfile` specifies the base model and any custom instructions for tailoring resume parsing to your needs.

**Modelfile Example:**
With:
```markdown
FROM granite3.3:2b

SYSTEM """
You are a strict resume parser.

Your job is to extract structured resume data into a **single valid JSON object** using the exact format below.
You must NOT infer, guess, or hallucinate any information not explicitly stated in the input.

Return JSON in this **exact structure and key order**:

{
  "name": string,
  "email": string,
  "phone": string,
  "socialMedia": {
    "linkedin": string or null,
    "github": string or null,
    "portfolio": string or null
  },
  "skills": [string],
  "education": [
    {
      "degree": string,
      "university": string,
      "startYear": string,
      "endYear": string,
      "CGPA": string
    }
  ],
  "experience": [
    {
      "company": string,
      "position": string,
      "from": string,
      "to": string,
      "description": string
    }
  ],
  "projects": [
    {
      "title": string,
      "description": string,
      "technologies": [string]
    }
  ],
  "languages": [
    {
      "language": string,
      "proficiency": string or null
    }
  ],
  "certifications": [
    {
      "name": string,
      "issuer": string
    }
  ]
}

📏 Rules:
- All keys must be lowercase and strictly follow the structure above.
- Always include all top-level fields, even if empty: return empty arrays `[]` or `null` for missing subfields.
- Do NOT guess or generate placeholder values like “Not specified”, “Unknown”, or empty strings.
- For missing `socialMedia` fields → set value to `null`.
- For missing `proficiency` in `languages` → set `"proficiency": null`.
- If any required subfield inside an object is missing, omit that object from the array.
- If multiple emails/phones are found, return the first valid one only.
- `email` must follow a valid format: e.g., name@example.com
- `phone` must be a valid number with 10 to 15 digits, optionally with country code. Remove all dashes, spaces, and parentheses.
- Dates like “Jan 2022 – Present” → `"from": "2022"`, `"to": "Present"`
- Output must be valid JSON — no markdown, no trailing commas, no comments.
- Never return any placeholder values like "Not specified", "N/A", or "Unknown".
- If required fields like `company`, `position`, or `description` are missing in any `experience` object, omit the entire object.
- For any optional field (like `proficiency`) that is missing, return `null`.
- Never duplicate any JSON keys.
- Do not include unrelated keys like `phone` or `email` under `experience`.
- Do not infer or assume technologies or proficiencies if not stated in the input.

🧪 Output a single strict JSON object only — no explanations or extra text.
"""
```

---

## Deployment

### Quickstart

1. **Start Kafka :**
    ```bash
    docker compose -f docker-kafka.yaml up -d
    ```

2. **Start Ollama (LLM API):**
    ```bash
    docker compose -f docker-ollama.yaml up -d
    ```

3. **Start the microservices stack:**
    ```bash
    docker compose up -d
    ```
   *(Or start individual microservices with Maven/Gradle as needed.)*

### Configuration

- The **resume-parser-service** connects to the Ollama API at `http://ollama:11434` and uses the IBM Granite 3.3:2b model as defined in the `Modelfile`.
- Kafka broker connection details are set in each service's `application.yaml`.

---

## Security

- **JWT-based authentication** via `auth-service`
- **API Gateway** enforces HTTP-only cookie-based session handling

---

## Monitoring

- **Spring Boot Admin** provides a live dashboard of service health and metrics.
- **Spring Boot Actuator** endpoints are enabled for detailed monitoring.

---

## API Gateway Routing

| Path Prefix           | Mapped Service               | Final Endpoint Pattern       |
| --------------------- | ---------------------------- | --------------------------- |
| `/jobs`               | `job-posting-service`        | `/api/jobs/**`              |
| `/applications`       | `application-submit-service` | `/api/applications/**`      |
| `/auth`               | `auth-service`               | `/api/auth/**`              |
| `/admin/users`        | `auth-service`               | `/api/admin/users/**`       |
| `/admin/jobs`         | `job-posting-service`        | `/api/admin/jobs/**`        |
| `/admin/applications` | `application-submit-service` | `/api/admin/applications/**`|

---

## Communication

- **Feign Clients** for RESTful calls between services.
- **Kafka** for event-driven flows (e.g., application submission triggers resume parsing via a Kafka message).
- **Ollama** (IBM Granite 3.3:2b LLM) is invoked by the resume parser service for semantic data extraction from uploaded resumes.

---

## Customization

- **Ollama LLM model** and prompt instructions can be tailored by editing `ollama/Modelfile`.
- **Kafka** configuration is managed via `docker-kafka.yaml`.
- **Service ports, gateway routes, and other parameters** can be modified in each service’s `application.yaml`.

---

## License

This project is open source and available under the [MIT License](LICENSE).

---

## Acknowledgements

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [Kafka](https://kafka.apache.org/)
- [Ollama](https://ollama.com/)
- [IBM Granite LLM](https://research.ibm.com/blog/granite-generative-ai-models)