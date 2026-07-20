# DevSpace - Developer Workspace Platform

A production-grade full-stack SaaS platform for software development teams.

## Tech Stack

| Layer | Technology |
|-------|------------|
| Frontend | React, Vite, Tailwind CSS, Zustand, React Router, Axios |
| Backend | Java 21, Spring Boot 3.2, Spring Security, JWT, Hibernate |
| Database | PostgreSQL |
| Tools | Docker, Maven, Git |

## Prerequisites

Install these before running:

1. **Java 21** - [Download](https://adoptium.net/)
2. **Maven 3.9+** - [Download](https://maven.apache.org/download.cgi)
3. **Node.js 18+** - [Download](https://nodejs.org/)
4. **Docker Desktop** - [Download](https://www.docker.com/products/docker-desktop/)

Verify installation:
```powershell
java -version
mvn -version
node -v
docker -version
```

## Quick Start

### Step 1: Start PostgreSQL

```powershell
cd d:\fullstack
docker compose up -d
```

This starts PostgreSQL on port `5432` with:
- Database: `devspace`
- Username: `devspace`
- Password: `devspace123`

### Step 2: Start Backend

```powershell
cd d:\fullstack\backend
mvn spring-boot:run
```

Backend runs at: **http://localhost:8080**

Swagger UI: **http://localhost:8080/swagger-ui.html**

### Step 3: Start Frontend

Open a new terminal:

```powershell
cd d:\fullstack\frontend
npm install
npm run dev
```

Frontend runs at: **http://localhost:5173**

## Default Login

| Field | Value |
|-------|-------|
| Email | `admin@devspace.com` |
| Password | `Admin@123` |

Or register a new account at `/register`.

## Project Structure

```
devspace/
├── backend/          # Spring Boot REST API
│   └── src/main/java/com/devspace/
│       ├── auth/         # Authentication & JWT
│       ├── project/      # Project management
│       ├── kanban/       # Kanban board
│       ├── dashboard/    # Dashboard stats
│       ├── apitester/    # API tester proxy
│       ├── github/       # GitHub integration
│       ├── docker/       # Docker dashboard
│       ├── deployment/   # Deployment tracking
│       ├── chat/         # Team chat
│       ├── database/     # DB explorer
│       ├── documentation/# Project docs
│       ├── profile/      # User profile
│       └── admin/        # Admin panel
├── frontend/         # React SPA
│   └── src/
│       ├── pages/        # All feature pages
│       ├── components/   # Reusable UI
│       ├── api/          # Axios & endpoints
│       └── store/        # Zustand auth store
└── docker-compose.yml
```

## Features

- **Authentication** - Register, Login, JWT, Refresh Token, RBAC
- **Dashboard** - Stats, activities, notifications
- **Projects** - CRUD, invite members, roles
- **Kanban** - Drag & drop tasks, comments, priorities
- **API Tester** - GET/POST/PUT/DELETE/PATCH with proxy
- **GitHub** - Connect repos, branches, pull requests
- **Docker** - Container & image management
- **Deployments** - Deploy, rollback, logs
- **Team Chat** - Group chat rooms
- **Database Explorer** - SQL runner, table browser
- **Documentation** - Markdown docs per project
- **Admin Panel** - Users, analytics, audit logs

## API Documentation

Once backend is running, visit:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Environment Variables

### Backend (`application.yml`)

| Variable | Default | Description |
|----------|---------|-------------|
| `JWT_SECRET` | (dev default) | JWT signing key |
| `FRONTEND_URL` | http://localhost:5173 | CORS origin |
| `MAIL_USERNAME` | (empty) | SMTP email |
| `GITHUB_CLIENT_ID` | (empty) | GitHub OAuth |

### Frontend (`.env`)

```
VITE_API_URL=http://localhost:8080
```

## Build for Production

```powershell
# Backend
cd backend
mvn clean package -DskipTests

# Frontend
cd frontend
npm run build
```

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `java` not recognized | Install JDK 21 and add to PATH |
| `mvn` not recognized | Install Maven and add to PATH |
| Database connection failed | Run `docker compose up -d` first |
| Port 8080 in use | Stop other apps or change `server.port` in `application.yml` |
| CORS errors | Ensure backend is running on port 8080 |

## License

MIT
