# COLLEGE MINI-PROJECT REPORT
## Digital Lost and Found System for College (CampusFind)

---

### **COVER PAGE**

| Field | Details |
| :--- | :--- |
| **Project Title** | **CampusFind — Digital Lost and Found System for College** |
| **Course / Subject** | Object-Oriented Programming using Java (OOP Java Laboratory Mini-Project) |
| **Student Name** | `[YOUR FULL NAME]` |
| **Roll Number / PRN** | `[YOUR ROLL NUMBER / UNIVERSITY REGISTRATION NO]` |
| **Branch / Department** | Computer Science & Engineering / Information Technology |
| **Semester / Year** | `[e.g., 3rd Year / Semester V]` |
| **Academic Year** | 2025 – 2026 |
| **Project Guide / Faculty** | `[PROFESSOR / GUIDE NAME]` |
| **Institution / College** | `[YOUR COLLEGE / UNIVERSITY NAME]` |

---

## 1. ABSTRACT
In high-density academic environments such as colleges and universities, personal possessions—including scientific calculators, earphones, laptops, textbooks, water bottles, and smart identity badges—are routinely misplaced. Traditional recovery mechanisms rely on fragmented WhatsApp/Telegram broadcast groups, physical bulletin noticeboards, or disorganized lost-and-found storage boxes at security checkpoints. These legacy approaches lack searchability, suffer from fraudulent ownership claims, and offer zero visibility into recovery status.

This project presents **CampusFind**, a full-stack, enterprise-grade web application engineered to digitize and safeguard the campus lost-and-found lifecycle. Developed using **Java 21**, **Spring Boot 3**, **Spring Data JPA**, **Spring Security with BCrypt**, and **clean vanilla JavaScript (Fetch API)**, CampusFind provides a unified platform where students can catalog lost and found articles, filter listings, and submit ownership claims accompanied by confidential proof. A dedicated Campus Administrator portal validates claims, eliminates conflicting approvals, and automatically transitions inventory states. The system also features an explainable similarity matching algorithm that scores potential matches based on keywords, category, campus location, and date proximity.

---

## 2. INTRODUCTION
Educational institutions encompass diverse facilities—such as lecture complexes, libraries, specialized engineering workshops, cafeterias, sports arenas, and parking bays. Managing misplaced items across these hubs requires an accessible, authenticated, and secure solution. 

CampusFind addresses this challenge by providing a centralized web portal accessible from any modern desktop or smartphone browser. By integrating the client interface directly within the Spring Boot application, the architecture operates without complex external multi-tier microservice overhead, making it exceptionally reliable and straightforward to demonstrate during academic evaluations.

---

## 3. PROBLEM STATEMENT
Manual and informal lost-and-found procedures in educational institutions suffer from critical operational defects:
1. **Lack of Centralization**: Inquiries are scattered across disparate departmental notice boards and peer messaging groups.
2. **High Vulnerability to False Claims**: Finders often publicly describe all features of a found item, enabling dishonest individuals to falsely claim valuable electronics.
3. **Absence of Lifecycle Tracking**: Students have no mechanism to check whether an inquiry has been reviewed, rejected, or collected.
4. **Administrative Bottleneck**: Campus facility staff lack structured logs of handed-over property and historical resolution rates.

---

## 4. OBJECTIVES
The core objectives of the CampusFind system are:
- To implement an authenticated and secure web portal for reporting lost and found possessions.
- To demonstrate core **Java Object-Oriented Programming (OOP)** paradigms in a real-world enterprise application.
- To introduce a **two-phase verification claim protocol** where private distinguishing details are visible exclusively to administrators.
- To deploy an **explainable algorithmic recommendation engine** that matches lost reports with candidate found listings.
- To provide live dashboard analytics calculated directly from database entities.

---

## 5. SCOPE OF THE PROJECT
- **In Scope**:
  - Secure registration and role-based login (STUDENT and ADMIN) with BCrypt password hashing.
  - Multi-attribute search and filtering by keywords, category, campus location, date, and state.
  - Ownership claim submission, withdrawal, and administrator decision workflows with automatic state synchronization.
  - Algorithmic candidate matching with explainable percentage scores.
  - Live activity notifications and audit logging.
  - Dual database compatibility (H2 for zero-config local runs and PostgreSQL for cloud production).
- **Out of Scope (Future Phases)**:
  - Native mobile application compilation (iOS/Android APK).
  - Computer-vision-based deep learning image recognition.
  - SMS gateway integration.

---

## 6. EXISTING VS. PROPOSED SYSTEM

| Feature / Dimension | Existing Informal Method | Proposed CampusFind System |
| :--- | :--- | :--- |
| **Medium** | Physical logbooks, WhatsApp groups | Centralized Web Portal |
| **Searchability** | None (manual reading required) | Multi-parameter instant filter & keyword search |
| **Identity Verification** | Verbal assertion | Role-based authentication & confidential proof fields |
| **Claim Resolution** | Unrecorded handovers | State-machine transitions (OPEN ➔ CLAIM_PENDING ➔ RETURNED) |
| **Duplicate Prevention** | None (susceptible to double handovers) | Backend concurrency lock & auto-rejection of rivals |
| **Suggestions** | Manual human recollection | Explainable matching algorithm (Category, Loc, Date, Words) |

---

## 7. SYSTEM REQUIREMENTS

### 7.1 Software Requirements
- **Programming Language**: Java 17 / Java 21 LTS
- **Backend Framework**: Spring Boot 3.2.x (Spring Web, Spring Security, Spring Data JPA, Bean Validation)
- **Frontend Technologies**: Semantic HTML5, Modern CSS3 (responsive grid/flexbox), Vanilla JavaScript (ES6+ Fetch API)
- **Databases**:
  - Development: H2 In-Memory Database (with web console)
  - Production: PostgreSQL 15+
- **Build & Dependency Management**: Apache Maven 3.9+ / Maven Wrapper (`mvnw`)
- **Containerization**: Docker & Docker Compose compatible

### 7.2 Hardware Requirements
- **Processor**: Intel Core i3 / AMD Ryzen 3 or higher
- **RAM**: Minimum 4 GB RAM (8 GB recommended)
- **Disk Storage**: 500 MB free hard disk space
- **Display**: Minimum resolution 1024x768 (Optimized for 1080p Smartboards and mobile phones)

---

## 8. JAVA OOP PRINCIPLES DEMONSTRATED

The CampusFind codebase was engineered to explicitly showcase fundamental and advanced Object-Oriented Programming paradigms:

1. **Encapsulation & Data Hiding**:
   - Entities (`User`, `Item`, `Claim`, `Notification`) declare all internal state variables with `private` access modifiers.
   - Access and mutations occur strictly through validated getters and setters.
   - Sensitive fields, such as encrypted password hashes, are annotated with `@JsonIgnore` to prevent serialization leakage.

2. **Inheritance & Abstraction**:
   - The abstract superclass `BaseEntity` centralizes generic audit fields (`id`, `createdAt`, `updatedAt`) and lifecycle hooks (`@PrePersist`, `@PreUpdate`).
   - Domain entities (`Item`, `Claim`, `Notification`) extend `BaseEntity`, promoting code reusability without redundant field declarations.

3. **Polymorphism**:
   - Dynamic polymorphism is utilized throughout Spring's service interfaces (`UserDetailsService` implementation `UserDetailsServiceImpl`).
   - Method overloading and polymorphic DTO transformation methods (`ItemResponse.fromEntity(item)` and overloaded variant with context credentials).

4. **Association, Composition & Aggregation**:
   - Relational associations are mapped using JPA relationship annotations:
     - `@ManyToOne` association linking `Item` to its reporting `User`.
     - `@ManyToOne` association linking `Claim` to its target `Item` and claiming `User`.
     - `@ManyToOne` association linking `Notification` to its recipient `User`.

5. **Separation of Concerns & Layered Architecture**:
   - Strict stratification across `controller` (HTTP serialization & routing), `service` (business logic & security authorization rules), `repository` (data persistence), and `dto` (data contract decoupled from entity schemas).
   - Inversion of Control (IoC) and Constructor-based Dependency Injection throughout.

---

## 9. MODULE DESCRIPTIONS

### 9.1 Authentication & Authorization Module
- **Endpoints**: `/api/auth/register`, `/api/auth/login`, `/api/auth/me`, `/api/auth/logout`.
- Uses Spring Security filter chain with BCrypt password hashing.
- Role-based access control restricts administrative actions (`ROLE_ADMIN`) while providing self-service facilities for students (`ROLE_STUDENT`).

### 9.2 Item Catalog & Reporting Module
- **Endpoints**: `/api/items`, `/api/items/{id}`, `/api/items/my`.
- Enables publication of Lost and Found reports with category classification, incident dates, locations, and photo URLs.
- Enforces ownership checks: only the original reporter or an administrator can update report details or transition an item's status.

### 9.3 Claim & Verification Module
- **Endpoints**: `/api/claims/items/{itemId}`, `/api/claims/my`, `/api/claims/{id}/withdraw`.
- Restricts claims exclusively to FOUND items (prevents claiming lost item reports).
- Prohibits students from claiming items they reported finding.
- Requires confidential verification proofs (e.g., distinguishing scratches, lock screen details, serial numbers).
- Enforces database uniqueness checks to prevent duplicate claims by the same student.

### 9.4 Administrator Review & Decision Desk
- **Endpoints**: `/api/admin/claims`, `/api/admin/claims/{id}/decision`, `/api/admin/items`.
- Provides authorized administrators with tools to inspect confidential verification proof.
- **Atomic Concurrency Guarantee**: When an administrator approves a claim, the item is marked as `RETURNED`, and all other rival pending claims for that item are automatically set to `REJECTED` with system notification messages.

### 9.5 Algorithmic Recommendation Engine
- **Service**: `ItemMatchingService`.
- Executes an explainable 4-factor scoring heuristic:
  1. Category classification match (+40%)
  2. Keyword token intersection across title & description (+30%)
  3. Campus location similarity (+20%)
  4. Date proximity within 3 days (+10%)
- Delivers a match confidence score accompanied by human-readable explanation bullet points.

---

## 10. DATABASE SCHEMA DESIGN

The persistence layer consists of four core relational tables:

```
+-----------------------------------------------------------------------------------------+
|                                    CAMPUSFIND SCHEMA                                    |
+-----------------------------------------------------------------------------------------+
| USERS         (id, full_name, email, password, role, enabled, created_at)               |
| ITEMS         (id, title, description, category, type, location, relevant_date,         |
|                image_url, status, user_id [FK], created_at, updated_at)                 |
| CLAIMS        (id, item_id [FK], claimant_id [FK], claim_explanation,                   |
|                verification_details, status, admin_notes, reviewer_id [FK], timestamps) |
| NOTIFICATIONS (id, recipient_id [FK], message, is_read, related_item_id, timestamps)    |
+-----------------------------------------------------------------------------------------+
```

---

## 11. TESTING & VERIFICATION RESULTS

Automated unit and integration test suites were constructed with **JUnit 5**, **Mockito**, and **Spring Boot Test**.

| Test Suite / Case | Class | Objective Tested | Result |
| :--- | :--- | :--- | :--- |
| **TC-01** | `CampusFindApplicationTests` | Verify full Spring application context boots with H2 | **PASSED** |
| **TC-02** | `ItemMatchingServiceTest` | Verify 4-factor algorithm calculates >70% match for related earbuds | **PASSED** |
| **TC-03** | `ClaimServiceTest` | Ensure reporter cannot claim their own found item | **PASSED** |
| **TC-04** | `ClaimServiceTest` | Ensure student cannot submit claim on a LOST report | **PASSED** |
| **TC-05** | `ClaimServiceTest` | Prevent duplicate active claims by identical student | **PASSED** |
| **TC-06** | `ClaimServiceTest` | Confirm admin approval marks item RETURNED and rejects rivals | **PASSED** |

---

## 12. SCREENSHOT CHECKLIST FOR VIVA PRESENTATION

When presenting to your faculty, capture and showcase these key screens:
1. **Landing Page (`/index.html`)**: Displaying the modern header, live statistics ticker, category cards, and recent campus listings.
2. **Directory & Live Filters (`/browse.html`)**: Demonstrating instant search and filtering by Category and Status.
3. **Item Details Page (`/item-details.html?id=1`)**: Highlighting the explainable "Suggested Matching Items" widget.
4. **Report Form (`/report.html`)**: Showcasing campus location quick-chips and form validation.
5. **Student Dashboard (`/dashboard.html`)**: Showing personal reports, submitted claims status, and notifications.
6. **Admin Review Desk (`/admin.html`)**: Demonstrating the inspection of secret verification proof and 1-click claim approval.
7. **H2 Database Console (`/h2-console`)**: Confirming real SQL relational tables and data persistence.

---

## 13. LIMITATIONS & FUTURE ENHANCEMENTS

### 13.1 Limitations
- Image attachments currently accept external HTTPS URLs rather than multi-part S3 bucket uploads.
- Notifications are persisted and polled via REST rather than bidirectional WebSocket streams.

### 13.2 Future Enhancements
- Integration of a mobile-friendly Progressive Web App (PWA) with push notification service workers.
- Automated optical character recognition (OCR) on uploaded photos of lost student ID cards.
- Barcode/QR code tagging for item handovers at physical security checkpoints.

---

## 14. CONCLUSION
The **Digital Lost and Found System for College (CampusFind)** successfully transitions a historically disorganized, manual campus routine into a streamlined, transparent, and auditable digital workflow. Engineered in strict accordance with Java Object-Oriented principles and Spring Boot best practices, the application delivers security, intuitive user experience, and practical campus value.

---

## 15. REFERENCES
1. Bloch, Joshua. *Effective Java (3rd Edition)*. Addison-Wesley Professional, 2018.
2. Walls, Craig. *Spring in Action (6th Edition)*. Manning Publications, 2022.
3. Spring Framework Official Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/
4. MDN Web Docs (Fetch API & Modern DOM Manipulation): https://developer.mozilla.org/
