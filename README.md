# CampusFind — Digital Lost and Found System for College

[![Java 17/21](https://img.shields.io/badge/Java-17%20%2F%2021-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-H2%20%7C%20PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-Academic-blue?style=for-the-badge)](LICENSE)

CampusFind is a full-stack college mini-project built with **Java OOP**, **Spring Boot 3**, and modern **vanilla JavaScript**. It replaces disorganized physical notice boards and informal WhatsApp groups with a centralized, secure, and auditable web platform for reporting, tracking, and claiming lost articles on campus.

---

## 🌟 Key Features

1. **Student & Admin Portals**: Dedicated, role-based workflows for students and facility administrators.
2. **Lost & Found Reporting**: Fast cataloging with campus location quick-chips, incident dates, and categories.
3. **Smart Search & Filters**: Search listings in real-time by keyword, item classification, location, and status.
4. **Fraud-Resistant Claims**: Submit ownership claims with private verification details (serial numbers, wallpaper marks) visible only to campus administrators.
5. **Admin Decision Desk**: One-click claim approval that marks items as `RETURNED` and automatically prevents conflicting duplicate handovers.
6. **Explainable Matching Algorithm**: Algorithmic suggestions that pair lost reports with found articles based on category, location, date proximity, and title keywords.
7. **Real Database Metrics**: Live statistics calculated dynamically from persistent entities.
8. **Real-Time Notifications**: In-app notifications alerting students when claims are submitted or decided.

---

## 🏗️ Technology Stack

- **Backend**: Java 17 / 21 LTS, Spring Boot 3.2.5 (Spring Web, Spring Security, Spring Data JPA, Bean Validation)
- **Frontend**: Semantic HTML5, Custom Responsive CSS3 Design System, Vanilla JavaScript (ES6+ Fetch API)
- **Database**:
  - Development: H2 In-Memory Database (with `/h2-console`)
  - Production: PostgreSQL (Cloud ready)
- **Security**: BCrypt password hashing, session-based cookie management, ownership authorization guards
- **Testing**: JUnit 5, Mockito, Spring Boot Test

---

## 🔑 Pre-Configured Demo Accounts

When running in development mode (`dev` profile), the system automatically seeds sample listings and test accounts:

| Role | Email Address | Password | Privileges |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin@campusfind.edu` | `Admin@123Password` | Review claims, inspect private proof, manage all listings |
| **Student 1** | `aarav.student@campusfind.edu` | `Password@123` | Report items, browse, claim, manage personal reports |
| **Student 2** | `diya.student@campusfind.edu` | `Password@123` | Report items, browse, claim, manage personal reports |

*(You can also register your own student account in seconds via `/register.html`)*

---

## 🚀 Quick Start Guide (Run Locally)

### Prerequisites
- **Java 17 or Java 21** installed on your computer.

### Step 1: Open Terminal in Project Directory
```bash
cd "c:\Users\OM NAMAH SHIVAYA\Downloads\OOP_PROJECT"
```

### Step 2: Start the Application
- **Windows (Command Prompt / PowerShell)**:
  ```cmd
  run.bat
  ```
  *Or directly using the Maven Wrapper:*
  ```cmd
  mvnw.cmd spring-boot:run
  ```
- **macOS / Linux**:
  ```bash
  ./mvnw spring-boot:run
  ```

### Step 3: Open in Chrome
Open your web browser and navigate to:
```
http://localhost:8080
```

---

## 📡 REST API Endpoints Overview

| Method | Endpoint | Access | Purpose |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Public | Register a new student account |
| `POST` | `/api/auth/login` | Public | Authenticate user and initiate session |
| `GET` | `/api/auth/me` | Authenticated | Retrieve current user profile |
| `POST` | `/api/auth/logout` | Authenticated | Terminate session |
| `GET` | `/api/items` | Public | Search and filter listings |
| `GET` | `/api/items/{id}` | Public | Retrieve item details |
| `GET` | `/api/items/{id}/matches` | Public | Get explainable matching recommendations |
| `POST` | `/api/items` | Student/Admin | Publish a new Lost or Found report |
| `PATCH`| `/api/items/{id}/status` | Owner/Admin | Transition item status (RESOLVED, etc.) |
| `POST` | `/api/claims/items/{id}` | Student | Submit an ownership claim for a found item |
| `GET` | `/api/claims/my` | Student | View personal submitted claims |
| `GET` | `/api/admin/claims` | Admin Only | Review all submitted ownership claims |
| `POST` | `/api/admin/claims/{id}/decision` | Admin Only | Approve or reject a claim with remarks |
| `GET` | `/api/stats` | Public | Retrieve real-time database metrics |
| `GET` | `/api/health` | Public | Service health & uptime indicator |

---

## 📚 Project Documentation & Diagrams

Complete documentation and GitHub-renderable Mermaid diagrams are available in the [`docs/`](./docs) folder:
- [System Architecture Diagram](./docs/architecture-diagram.md)
- [Class Diagram (OOP Architecture)](./docs/class-diagram.md)
- [Use Case Diagram](./docs/use-case-diagram.md)
- [Sequence Diagram: Report Lost Item](./docs/sequence-report-item.md)
- [Sequence Diagram: Claim Submission & Approval](./docs/sequence-claim-approval.md)
- [Activity Diagram: Recovery Workflow](./docs/activity-diagram.md)
- [Entity Relationship (ER) Diagram](./docs/er-diagram.md)
- [Comprehensive Formal Mini-Project Report](./docs/PROJECT_REPORT.md)

---

## ☁️ Public Cloud Deployment (Render.com)

1. Push this project to your GitHub repository:
   ```bash
   git init
   git add .
   git commit -m "Initial commit: CampusFind complete system"
   git branch -M main
   git remote add origin https://github.com/<YOUR_USERNAME>/digital-lost-and-found-system.git
   git push -u origin main
   ```
2. Log in to [Render.com](https://render.com/) and click **New + ➔ Blueprint**.
3. Select your repository. Render will automatically detect [`render.yaml`](./render.yaml) and deploy the Spring Boot Docker container and a PostgreSQL database.
4. Share the generated HTTPS link (e.g. `https://campusfind-web.onrender.com`) with your professor!
