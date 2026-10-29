# CampusFind — System Architecture Diagram

```mermaid
flowchart TB
    subgraph ClientLayer ["Client Layer (Responsive Browser UI)"]
        HTML["Semantic HTML5 (index, browse, item-details, report, dashboard, admin)"]
        CSS["Modern CSS3 Design System (White, Navy, Teal, Amber)"]
        JS["Vanilla JS ES6 Modules (Fetch API, DOM manipulation, Toasts)"]
    end

    subgraph SecurityLayer ["Security & Routing Layer"]
        Filter["Spring Security Filter Chain"]
        AuthMgr["AuthenticationManager & BCrypt PasswordEncoder"]
        SessionMgr["HTTP Session Management (JSESSIONID)"]
    end

    subgraph ControllerLayer ["REST Controller Layer (/api)"]
        AuthCtrl["AuthController (/api/auth)"]
        ItemCtrl["ItemController (/api/items)"]
        ClaimCtrl["ClaimController (/api/claims)"]
        AdminCtrl["AdminController (/api/admin)"]
        NotifCtrl["NotificationController (/api/notifications)"]
        StatsCtrl["StatsController (/api/stats)"]
        HealthCtrl["HealthController (/api/health)"]
    end

    subgraph ServiceLayer ["Business & OOP Domain Services"]
        UserSvc["UserService"]
        ItemSvc["ItemService"]
        ClaimSvc["ClaimService"]
        NotifSvc["NotificationService"]
        MatchingSvc["ItemMatchingService (Explainable Recommendations)"]
        StatsSvc["DashboardStatsService"]
    end

    subgraph DataAccessLayer ["Data Access Layer (Spring Data JPA)"]
        UserRepo["UserRepository"]
        ItemRepo["ItemRepository"]
        ClaimRepo["ClaimRepository"]
        NotifRepo["NotificationRepository"]
    end

    subgraph DatabaseLayer ["Persistence Layer"]
        H2[("H2 In-Memory DB (Local Dev)")]
        PG[("PostgreSQL (Production Cloud)")]
    end

    ClientLayer <-->|JSON over HTTP(S)| SecurityLayer
    SecurityLayer --> ControllerLayer
    ControllerLayer --> ServiceLayer
    ServiceLayer --> DataAccessLayer
    DataAccessLayer --> H2
    DataAccessLayer -.->|when SPRING_PROFILES_ACTIVE=prod| PG
```
