# AI-Powered Smart Student Notebook Portal

Full-stack platform for uploading student notes, AI summaries, smart search, and verification workflows.

## Tech Stack

- Frontend: React, Vite, Tailwind CSS, React Router, Axios, Chart.js
- Backend: Spring Boot, Spring Security, JWT, JPA/Hibernate, Maven
- Database: PostgreSQL

## Project Structure

```
frontend/
backend/
README.md
```

## Backend Setup

1. Configure PostgreSQL and create a database called `notebook_portal`.
2. Update environment variables (or use defaults in application properties).

### Environment Variables

```
DB_URL=jdbc:postgresql://localhost:5432/notebook_portal
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=change-me
JWT_EXPIRATION_MS=86400000
UPLOAD_DIR=uploads
AI_ENABLED=false
OPENAI_API_KEY=
OPENAI_MODEL=gpt-4o-mini
OCR_ENABLED=false
```

### Run Backend

```
cd backend
./mvnw spring-boot:run
```

## Frontend Setup

```
cd frontend
npm install
npm run dev
```

### Frontend Environment Variables

Create `frontend/.env`:

```
VITE_API_BASE_URL=http://localhost:8080
```

## Core API Endpoints

- Auth
  - `POST /api/auth/register`
  - `POST /api/auth/login`
- Notebooks
  - `POST /api/notebooks/upload`
  - `GET /api/notebooks/search`
  - `GET /api/notebooks/{id}`
  - `POST /api/notebooks/{id}/download`
  - `GET /api/notebooks/{id}/ai/summary`
  - `GET /api/notebooks/{id}/ai/questions`
  - `GET /api/notebooks/{id}/ai/tags`
- Reviews
  - `POST /api/notebooks/{id}/reviews`
- Verification
  - `POST /api/notebooks/{id}/verify`
- Admin
  - `GET /api/admin/stats`

## Deployment Guide

### Frontend (Vercel)

1. Push frontend to GitHub.
2. Import repo in Vercel.
3. Set `VITE_API_BASE_URL` to your backend URL.
4. Deploy.

### Backend (Render/Railway)

1. Push backend to GitHub.
2. Create a new Web Service.
3. Set environment variables listed above.
4. Use build command: `./mvnw clean package`
5. Start command: `java -jar target/notebook-portal-0.0.1-SNAPSHOT.jar`

### PostgreSQL (Render/Railway/Neon)

1. Create a new PostgreSQL instance.
2. Update `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` on your backend deployment.

## Roadmap

- Replace AI placeholder with OpenAI API integration
- Add OCR extraction pipeline
- Add faculty verification dashboard
- Build analytics charts with Chart.js
