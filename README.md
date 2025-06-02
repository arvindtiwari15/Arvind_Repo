# Movie Booking Platform

A full-stack movie booking platform built with Spring Boot, React, and Docker.

## Project Structure

```
movie-booking/
├── backend/                 # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/moviebooking/
│   │   │   │       ├── config/         # Configuration classes
│   │   │   │       ├── controller/     # REST controllers
│   │   │   │       ├── dto/           # Data Transfer Objects
│   │   │   │       ├── exception/     # Custom exceptions
│   │   │   │       ├── model/         # Entity classes
│   │   │   │       ├── repository/    # JPA repositories
│   │   │   │       ├── security/      # Security configuration
│   │   │   │       ├── service/       # Business logic
│   │   │   │       └── MovieBookingApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/           # Test classes
│   ├── Dockerfile
│   └── pom.xml
├── frontend/               # React frontend
│   ├── src/
│   │   ├── components/    # React components
│   │   ├── pages/        # Page components
│   │   ├── services/     # API services
│   │   ├── utils/        # Utility functions
│   │   └── App.js
│   ├── Dockerfile
│   └── package.json
├── database/
│   └── init.sql          # Database initialization script
├── docker-compose.yml    # Docker Compose configuration
└── README.md
```

## Completed Features

### Backend
- ✅ Project structure setup
- ✅ Database schema design
- ✅ User authentication and authorization
- ✅ JWT token implementation
- ✅ Basic CRUD operations for:
  - Users
  - Theatres
  - Movies
  - Shows
  - Bookings

### Frontend
- ✅ Project structure setup
- ✅ Basic UI components
- ✅ Authentication pages (Login/Register)
- ✅ Protected routes
- ✅ API integration with backend

### Infrastructure
- ✅ Docker configuration
- ✅ PostgreSQL database setup
- ✅ Docker Compose configuration
- ✅ Environment variables setup

## Prerequisites

- Docker and Docker Compose
- Java 17 or higher
- Node.js 16 or higher
- PostgreSQL 15 (if running without Docker)

## Running the Application Locally

### Using Docker (Recommended)

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd movie-booking
   ```

2. Use the provided script to start the application:
   ```bash
   ./start.sh
   ```
   This script checks if Docker is running and if containers are already running. If containers are already up, it will notify you and exit.

3. Access the application:
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080
   - Database: localhost:5432

### Running Without Docker

#### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project:
   ```bash
   ./mvnw clean install
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

#### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

## Running Tests

### About the Tests

This project includes only two very simple backend tests for demonstration purposes:
- `ControllerContextTest`: Verifies that the Spring context loads for controllers.
- `ServiceContextTest`: Verifies that the Spring context loads for services.

These tests do not require any test data or business logic and are designed to always pass as long as the application starts up correctly.

### How to Run Tests (Docker)

To run the backend tests inside Docker, use the following command from the project root:

```bash
docker compose exec backend mvn test
```

- This will run the tests inside the running backend container.
- You should see output indicating that 2 tests ran and both passed.

### How to Run Tests (Locally)

If you want to run the tests locally (without Docker):

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Run the tests:
   ```bash
   ./mvnw test
   ```

---

## Database Configuration

The application uses PostgreSQL with the following default configuration:

- Database: movie_booking
- Username: postgres
- Password: postgres_password
- Port: 5432

The database schema is automatically initialized when the PostgreSQL container starts up.

## API Documentation

The backend API documentation is available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Feature Testing Guide

Below are cURL commands and expected results to test the main features of the backend API.

### 1. User Registration

**Endpoint:**  `POST /api/auth/register`

Registers a new user (role: USER, THEATRE_OWNER, or ADMIN).

**Sample cURL:**
```sh
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test@123",
    "fullName": "Test User",
    "role": "THEATRE_OWNER"
  }'
```
**Expected Result:**
- 201 Created (or 200 OK)
- JSON response with user details and a JWT token:
  ```json
  {
    "id": "uuid-string",
    "email": "test@example.com",
    "fullName": "Test User",
    "role": "THEATRE_OWNER",
    "token": "jwt-token-string"
  }
  ```
- If email already exists: 400/409 with error message.

---

### 2. User Login

**Endpoint:**  `POST /api/auth/login`

Authenticates a user and returns a JWT token.

**Sample cURL:**
```sh
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test@123"
  }'
```
**Expected Result:**
- 200 OK
- JSON response with user details and a JWT token (same as registration).

---

### 3. Theatre Management

#### a. Create Theatre

**Endpoint:**  `POST /api/theatres`

Creates a new theatre (requires THEATRE_OWNER role).

**Sample cURL:**
```sh
curl -X POST http://localhost:8080/api/theatres \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "name": "PVR Cinemas",
    "address": "123 Main St",
    "city": "Mumbai",
    "state": "Maharashtra",
    "country": "India"
  }'
```
**Expected Result:**
- 201 Created (or 200 OK)
- JSON response with theatre details.
- If not authorized: 403 Forbidden

#### b. Get All Theatres

**Endpoint:**  `GET /api/theatres`

**Sample cURL:**
```sh
curl -X GET http://localhost:8080/api/theatres \
  -H "Authorization: Bearer <jwt-token>"
```
**Expected Result:**
- 200 OK
- JSON array of theatre objects.

#### c. Get Theatres by City

**Endpoint:**  `GET /api/theatres/city/{city}`

**Sample cURL:**
```sh
curl -X GET http://localhost:8080/api/theatres/city/Mumbai \
  -H "Authorization: Bearer <jwt-token>"
```
**Expected Result:**
- 200 OK
- JSON array of theatres in the specified city.

#### d. Update Theatre

**Endpoint:**  `PUT /api/theatres/{id}`

**Sample cURL:**
```sh
curl -X PUT http://localhost:8080/api/theatres/<theatre-id> \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "name": "PVR Icon",
    "address": "456 New St",
    "city": "Mumbai",
    "state": "Maharashtra",
    "country": "India"
  }'
```
**Expected Result:**
- 200 OK
- Updated theatre object.

#### e. Delete Theatre

**Endpoint:**  `DELETE /api/theatres/{id}`

**Sample cURL:**
```sh
curl -X DELETE http://localhost:8080/api/theatres/<theatre-id> \
  -H "Authorization: Bearer <jwt-token>"
```
**Expected Result:**
- 204 No Content (or 200 OK with confirmation message)
- If not authorized: 403 Forbidden

---

**Note:**
- Replace `<jwt-token>` with the token received from registration/login.
- Replace `<theatre-id>` with the actual UUID of the theatre.
- All protected endpoints require the `Authorization: Bearer <jwt-token>` header.

### 4. Movie Management

#### Endpoints

- **Create Movie**  
  `POST /api/movies`  
  Create a new movie.  
  **Request Body:**  
  ```json
  {
    "title": "Inception",
    "description": "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
    "language": "English",
    "genre": "Sci-Fi",
    "durationMinutes": 148,
    "releaseDate": "2025-07-15T00:00:00",
    "posterUrl": "https://example.com/inception.jpg",
    "trailerUrl": "https://example.com/inception-trailer.mp4",
    "director": "Christopher Nolan",
    "castMembers": "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page",
    "rating": 8.8
  }
  ```  
  **Response:**  
  ```json
  {
    "id": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
    "title": "Inception",
    "description": "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
    "language": "English",
    "genre": "Sci-Fi",
    "durationMinutes": 148,
    "releaseDate": "2025-07-15T00:00:00",
    "posterUrl": "https://example.com/inception.jpg",
    "trailerUrl": "https://example.com/inception-trailer.mp4",
    "director": "Christopher Nolan",
    "castMembers": "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page",
    "rating": 8.8,
    "createdAt": "2025-06-01T14:47:48.415672",
    "updatedAt": "2025-06-01T14:47:48.415672"
  }
  ```

- **Get All Movies**  
  `GET /api/movies`  
  Retrieve a list of all movies.  
  **Response:**  
  ```json
  [
    {
      "id": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
      "title": "Inception",
      "description": "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
      "language": "English",
      "genre": "Sci-Fi",
      "durationMinutes": 148,
      "releaseDate": "2025-07-15T00:00:00",
      "posterUrl": "https://example.com/inception.jpg",
      "trailerUrl": "https://example.com/inception-trailer.mp4",
      "director": "Christopher Nolan",
      "castMembers": "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page",
      "rating": 8.8,
      "createdAt": "2025-06-01T14:47:48.415672",
      "updatedAt": "2025-06-01T14:47:48.415672"
    }
  ]
  ```

- **Update Movie**  
  `PUT /api/movies/{id}`  
  Update an existing movie.  
  **Request Body:**  
  ```json
  {
    "title": "Inception Updated",
    "description": "Updated description for Inception.",
    "language": "English",
    "genre": "Sci-Fi",
    "durationMinutes": 150,
    "releaseDate": "2025-07-15T00:00:00",
    "posterUrl": "https://example.com/inception-updated.jpg",
    "trailerUrl": "https://example.com/inception-trailer-updated.mp4",
    "director": "Christopher Nolan",
    "castMembers": "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page",
    "rating": 9.0
  }
  ```  
  **Response:**  
  ```json
  {
    "id": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
    "title": "Inception Updated",
    "description": "Updated description for Inception.",
    "language": "English",
    "genre": "Sci-Fi",
    "durationMinutes": 150,
    "releaseDate": "2025-07-15T00:00:00",
    "posterUrl": "https://example.com/inception-updated.jpg",
    "trailerUrl": "https://example.com/inception-trailer-updated.mp4",
    "director": "Christopher Nolan",
    "castMembers": "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page",
    "rating": 9.0,
    "createdAt": "2025-06-01T14:47:48.415672",
    "updatedAt": "2025-06-01T14:47:48.415672"
  }
  ```

- **Delete Movie**  
  `DELETE /api/movies/{id}`  
  Delete a movie by its ID.  
  **Response:**  
  `204 No Content`

- **Search Movies**  
  `GET /api/movies/search?title=Inception`  
  Search for movies by title.  
  **Response:**  
  ```json
  []
  ```

- **Get Movies by Genre**  
  `GET /api/movies/genre/Sci-Fi`  
  Retrieve movies by genre.  
  **Response:**  
  ```json
  [
    {
      "id": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
      "title": "Inception",
      "description": "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
      "language": "English",
      "genre": "Sci-Fi",
      "durationMinutes": 148,
      "releaseDate": "2025-07-15T00:00:00",
      "posterUrl": "https://example.com/inception.jpg",
      "trailerUrl": "https://example.com/inception-trailer.mp4",
      "director": "Christopher Nolan",
      "castMembers": "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page",
      "rating": 8.8,
      "createdAt": "2025-06-01T14:47:48.415672",
      "updatedAt": "2025-06-01T14:47:48.415672"
    }
  ]
  ```

- **Get Movies by Language**  
  `GET /api/movies/language/English`  
  Retrieve movies by language.  
  **Response:**  
  ```json
  [
    {
      "id": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
      "title": "Inception",
      "description": "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
      "language": "English",
      "genre": "Sci-Fi",
      "durationMinutes": 148,
      "releaseDate": "2025-07-15T00:00:00",
      "posterUrl": "https://example.com/inception.jpg",
      "trailerUrl": "https://example.com/inception-trailer.mp4",
      "director": "Christopher Nolan",
      "castMembers": "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page",
      "rating": 8.8,
      "createdAt": "2025-06-01T14:47:48.415672",
      "updatedAt": "2025-06-01T14:47:48.415672"
    }
  ]
  ```

- **Get Upcoming Movies**  
  `GET /api/movies/upcoming`  
  Retrieve movies that are set to release in the future.  
  **Response:**  
  ```json
  [
    {
      "id": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
      "title": "Inception",
      "description": "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
      "language": "English",
      "genre": "Sci-Fi",
      "durationMinutes": 148,
      "releaseDate": "2025-07-15T00:00:00",
      "posterUrl": "https://example.com/inception.jpg",
      "trailerUrl": "https://example.com/inception-trailer.mp4",
      "director": "Christopher Nolan",
      "castMembers": "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page",
      "rating": 8.8,
      "createdAt": "2025-06-01T14:47:48.415672",
      "updatedAt": "2025-06-01T14:47:48.415672"
    }
  ]
  ```

- **Get Top Rated Movies**  
  `GET /api/movies/top-rated?minRating=8.0`  
  Retrieve movies with a rating above a specified minimum.  
  **Response:**  
  ```json
  [
    {
      "id": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
      "title": "Inception",
      "description": "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
      "language": "English",
      "genre": "Sci-Fi",
      "durationMinutes": 148,
      "releaseDate": "2025-07-15T00:00:00",
      "posterUrl": "https://example.com/inception.jpg",
      "trailerUrl": "https://example.com/inception-trailer.mp4",
      "director": "Christopher Nolan",
      "castMembers": "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page",
      "rating": 8.8,
      "createdAt": "2025-06-01T14:47:48.415672",
      "updatedAt": "2025-06-01T14:47:48.415672"
    }
  ]
  ```

### 5. Show Management

#### Endpoints

- **Create Show**  
  `POST /api/shows`  
  Create a new show (requires THEATRE_OWNER role).  
  **Request Body:**  
  ```json
  {
    "movieId": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
    "theatreId": "123e4567-e89b-12d3-a456-426614174000",
    "startTime": "2025-07-15T14:00:00",
    "endTime": "2025-07-15T16:30:00",
    "price": 250.0,
    "totalSeats": 100,
    "screenNumber": "Screen 1"
  }
  ```  
  **Response:**  
  ```json
  {
    "id": "123e4567-e89b-12d3-a456-426614174001",
    "movieId": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
    "movieTitle": "Inception",
    "theatreId": "123e4567-e89b-12d3-a456-426614174000",
    "theatreName": "PVR Cinemas",
    "startTime": "2025-07-15T14:00:00",
    "endTime": "2025-07-15T16:30:00",
    "price": 250.0,
    "totalSeats": 100,
    "availableSeats": 100,
    "screenNumber": "Screen 1",
    "createdAt": "2025-06-01T14:47:48.415672",
    "updatedAt": "2025-06-01T14:47:48.415672"
  }
  ```

- **Get Show by ID**  
  `GET /api/shows/{id}`  
  Retrieve a show by its ID.  
  **Response:**  
  ```json
  {
    "id": "123e4567-e89b-12d3-a456-426614174001",
    "movieId": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
    "movieTitle": "Inception",
    "theatreId": "123e4567-e89b-12d3-a456-426614174000",
    "theatreName": "PVR Cinemas",
    "startTime": "2025-07-15T14:00:00",
    "endTime": "2025-07-15T16:30:00",
    "price": 250.0,
    "totalSeats": 100,
    "availableSeats": 100,
    "screenNumber": "Screen 1",
    "createdAt": "2025-06-01T14:47:48.415672",
    "updatedAt": "2025-06-01T14:47:48.415672"
  }
  ```

- **Get All Shows**  
  `GET /api/shows`  
  Retrieve a list of all shows.  
  **Response:**  
  ```json
  [
    {
      "id": "123e4567-e89b-12d3-a456-426614174001",
      "movieId": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
      "movieTitle": "Inception",
      "theatreId": "123e4567-e89b-12d3-a456-426614174000",
      "theatreName": "PVR Cinemas",
      "startTime": "2025-07-15T14:00:00",
      "endTime": "2025-07-15T16:30:00",
      "price": 250.0,
      "totalSeats": 100,
      "availableSeats": 100,
      "screenNumber": "Screen 1",
      "createdAt": "2025-06-01T14:47:48.415672",
      "updatedAt": "2025-06-01T14:47:48.415672"
    }
  ]
  ```

- **Get Shows by Theatre**  
  `GET /api/shows/theatre/{theatreId}`  
  Retrieve shows for a specific theatre.  
  **Response:**  
  ```json
  [
    {
      "id": "123e4567-e89b-12d3-a456-426614174001",
      "movieId": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
      "movieTitle": "Inception",
      "theatreId": "123e4567-e89b-12d3-a456-426614174000",
      "theatreName": "PVR Cinemas",
      "startTime": "2025-07-15T14:00:00",
      "endTime": "2025-07-15T16:30:00",
      "price": 250.0,
      "totalSeats": 100,
      "availableSeats": 100,
      "screenNumber": "Screen 1",
      "createdAt": "2025-06-01T14:47:48.415672",
      "updatedAt": "2025-06-01T14:47:48.415672"
    }
  ]
  ```

- **Get Shows by Movie**  
  `GET /api/shows/movie/{movieId}`  
  Retrieve shows for a specific movie.  
  **Response:**  
  ```json
  [
    {
      "id": "123e4567-e89b-12d3-a456-426614174001",
      "movieId": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
      "movieTitle": "Inception",
      "theatreId": "123e4567-e89b-12d3-a456-426614174000",
      "theatreName": "PVR Cinemas",
      "startTime": "2025-07-15T14:00:00",
      "endTime": "2025-07-15T16:30:00",
      "price": 250.0,
      "totalSeats": 100,
      "availableSeats": 100,
      "screenNumber": "Screen 1",
      "createdAt": "2025-06-01T14:47:48.415672",
      "updatedAt": "2025-06-01T14:47:48.415672"
    }
  ]
  ```

- **Get Shows by Theatre and Date Range**  
  `GET /api/shows/theatre/{theatreId}/date-range?start=2025-07-15T00:00:00&end=2025-07-16T00:00:00`  
  Retrieve shows for a specific theatre within a date range.  
  **Response:**  
  ```json
  [
    {
      "id": "123e4567-e89b-12d3-a456-426614174001",
      "movieId": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
      "movieTitle": "Inception",
      "theatreId": "123e4567-e89b-12d3-a456-426614174000",
      "theatreName": "PVR Cinemas",
      "startTime": "2025-07-15T14:00:00",
      "endTime": "2025-07-15T16:30:00",
      "price": 250.0,
      "totalSeats": 100,
      "availableSeats": 100,
      "screenNumber": "Screen 1",
      "createdAt": "2025-06-01T14:47:48.415672",
      "updatedAt": "2025-06-01T14:47:48.415672"
    }
  ]
  ```

- **Get Shows by Movie and Date Range**  
  `GET /api/shows/movie/{movieId}/date-range?start=2025-07-15T00:00:00&end=2025-07-16T00:00:00`  
  Retrieve shows for a specific movie within a date range.  
  **Response:**  
  ```json
  [
    {
      "id": "123e4567-e89b-12d3-a456-426614174001",
      "movieId": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
      "movieTitle": "Inception",
      "theatreId": "123e4567-e89b-12d3-a456-426614174000",
      "theatreName": "PVR Cinemas",
      "startTime": "2025-07-15T14:00:00",
      "endTime": "2025-07-15T16:30:00",
      "price": 250.0,
      "totalSeats": 100,
      "availableSeats": 100,
      "screenNumber": "Screen 1",
      "createdAt": "2025-06-01T14:47:48.415672",
      "updatedAt": "2025-06-01T14:47:48.415672"
    }
  ]
  ```

- **Update Show**  
  `PUT /api/shows/{id}`  
  Update an existing show (requires THEATRE_OWNER role).  
  **Request Body:**  
  ```json
  {
    "movieId": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
    "theatreId": "123e4567-e89b-12d3-a456-426614174000",
    "startTime": "2025-07-15T15:00:00",
    "endTime": "2025-07-15T17:30:00",
    "price": 300.0,
    "totalSeats": 100,
    "screenNumber": "Screen 1"
  }
  ```  
  **Response:**  
  ```json
  {
    "id": "123e4567-e89b-12d3-a456-426614174001",
    "movieId": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
    "movieTitle": "Inception",
    "theatreId": "123e4567-e89b-12d3-a456-426614174000",
    "theatreName": "PVR Cinemas",
    "startTime": "2025-07-15T15:00:00",
    "endTime": "2025-07-15T17:30:00",
    "price": 300.0,
    "totalSeats": 100,
    "availableSeats": 100,
    "screenNumber": "Screen 1",
    "createdAt": "2025-06-01T14:47:48.415672",
    "updatedAt": "2025-06-01T14:47:48.415672"
  }
  ```

- **Delete Show**  
  `DELETE /api/shows/{id}`  
  Delete a show by its ID (requires THEATRE_OWNER role).  
  **Response:**  
  `204 No Content`

**Note:**
- Replace `<jwt-token>` with the token received from registration/login.
- Replace `<show-id>`, `<movie-id>`, and `<theatre-id>` with the actual UUIDs.
- All protected endpoints require the `Authorization: Bearer <jwt-token>` header.
- Date-time parameters should be in ISO-8601 format (e.g., "2025-07-15T14:00:00").

### 6. Theatre Search

#### Endpoints

- **Get Theatres by Movie, City, and Date**  
  `GET /api/theatres/movie/{movieId}/city/{city}/date/{date}`  
  Retrieve theatres running a specific movie in a city on a given date.  
  **Parameters:**
  - `movieId`: UUID of the movie
  - `city`: City name
  - `date`: Date in ISO-8601 format (e.g., "2025-07-15T00:00:00")
  
  **Response:**  
  ```json
  [
    {
      "theatre": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "name": "PVR Cinemas",
        "address": "123 Main St",
        "city": "Mumbai",
        "state": "Maharashtra",
        "country": "India",
        "ownerId": "123e4567-e89b-12d3-a456-426614174001",
        "ownerName": "John Doe",
        "createdAt": "2025-06-01T14:47:48.415672",
        "updatedAt": "2025-06-01T14:47:48.415672"
      },
      "shows": [
        {
          "id": "123e4567-e89b-12d3-a456-426614174002",
          "movieId": "857c60a6-5926-4a10-b8d8-5ae83239fd88",
          "movieTitle": "Inception",
          "theatreId": "123e4567-e89b-12d3-a456-426614174000",
          "theatreName": "PVR Cinemas",
          "startTime": "2025-07-15T14:00:00",
          "endTime": "2025-07-15T16:30:00",
          "price": 250.0,
          "totalSeats": 100,
          "availableSeats": 80,
          "screenNumber": "Screen 1",
          "createdAt": "2025-06-01T14:47:48.415672",
          "updatedAt": "2025-06-01T14:47:48.415672"
        }
      ]
    }
  ]
  ```

### 7. Booking Management

#### Endpoints

- **Create Booking**  
  `POST /api/bookings`  
  Create a new booking for a show (requires USER role).  
  **Request Body:**  
  ```json
  {
    "showId": "123e4567-e89b-12d3-a456-426614174002",
    "numberOfSeats": 2
  }
  ```  
  **Response:**  
  ```json
  {
    "id": "123e4567-e89b-12d3-a456-426614174003",
    "userId": "123e4567-e89b-12d3-a456-426614174004",
    "userName": "John Doe",
    "showId": "123e4567-e89b-12d3-a456-426614174002",
    "movieTitle": "Inception",
    "theatreName": "PVR Cinemas",
    "showTime": "2025-07-15T14:00:00",
    "numberOfSeats": 2,
    "totalAmount": 500.0,
    "status": "CONFIRMED",
    "createdAt": "2025-06-01T14:47:48.415672",
    "updatedAt": "2025-06-01T14:47:48.415672"
  }
  ```

- **Get User Bookings**  
  `GET /api/bookings`  
  Retrieve all bookings for the current user (requires USER role).  
  **Response:**  
  ```json
  [
    {
      "id": "123e4567-e89b-12d3-a456-426614174003",
      "userId": "123e4567-e89b-12d3-a456-426614174004",
      "userName": "John Doe",
      "showId": "123e4567-e89b-12d3-a456-426614174002",
      "movieTitle": "Inception",
      "theatreName": "PVR Cinemas",
      "showTime": "2025-07-15T14:00:00",
      "numberOfSeats": 2,
      "totalAmount": 500.0,
      "status": "CONFIRMED",
      "createdAt": "2025-06-01T14:47:48.415672",
      "updatedAt": "2025-06-01T14:47:48.415672"
    }
  ]
  ```

- **Get Booking by ID**  
  `GET /api/bookings/{id}`  
  Retrieve a specific booking by ID (requires USER role).  
  **Response:**  
  ```json
  {
    "id": "123e4567-e89b-12d3-a456-426614174003",
    "userId": "123e4567-e89b-12d3-a456-426614174004",
    "userName": "John Doe",
    "showId": "123e4567-e89b-12d3-a456-426614174002",
    "movieTitle": "Inception",
    "theatreName": "PVR Cinemas",
    "showTime": "2025-07-15T14:00:00",
    "numberOfSeats": 2,
    "totalAmount": 500.0,
    "status": "CONFIRMED",
    "createdAt": "2025-06-01T14:47:48.415672",
    "updatedAt": "2025-06-01T14:47:48.415672"
  }
  ```

- **Cancel Booking**  
  `DELETE /api/bookings/{id}`  
  Cancel a booking (requires USER role).  
  **Response:**  
  `204 No Content`

**Note:**
- Replace `<jwt-token>` with the token received from registration/login.
- Replace `<booking-id>`, `<show-id>`, `<movie-id>`, and `<theatre-id>` with the actual UUIDs.
- All protected endpoints require the `Authorization: Bearer <jwt-token>` header.
- Date-time parameters should be in ISO-8601 format (e.g., "2025-07-15T14:00:00").
- Booking cancellation is only allowed before the show starts.
- Seat availability is automatically managed when creating or cancelling bookings.

## Environment Variables

### Backend (.env)
```

## JWT Authentication & Protected Endpoint Testing (Debug Session)

### What Was Tested
- Registration and login endpoints to obtain a JWT token.
- Using the JWT token to access protected endpoints (e.g., `/api/theatres/...`).
- Ensured the backend uses the correct JWT secret from environment variables or `application.properties`.
- Debugged signature mismatch errors by:
  - Adding detailed logging to `JwtTokenProvider` and security config.
  - Verifying the secret is loaded and used consistently.
  - Ensuring tokens are generated and validated with the same secret.

### Testing Steps

1. **Register a New User**
   ```sh
   curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "email": "testuser@example.com",
       "password": "TestPassword123",
       "fullName": "Test User"
     }'
   ```
   - Or login if already registered:
   ```sh
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{
       "email": "testuser@example.com",
       "password": "TestPassword123"
     }'
   ```
   - **Expected:** JSON response with a `token` field (JWT).

2. **Test a Protected Endpoint**
   ```sh
   curl -X GET http://localhost:8080/api/theatres/movie/<movieId>/city/<city>/date/<date> \
     -H "Authorization: Bearer <paste-token-here>"
   ```
   - Replace `<paste-token-here>` with the token from step 1.
   - **Expected:**
     - 200 OK and valid data if token is valid and user is authorized.
     - 403 Forbidden if token is invalid, expired, or missing.

### Debugging & Expected Results
- If you see `JWT signature does not match locally computed signature` in logs:
  - Ensure the token was generated by the current backend (not an old or different environment).
  - Confirm the backend is using the correct secret (see logs for `[JwtTokenProvider] Initializing with JWT secret:`).
- After fixing configuration and using a fresh token:
  - **Expected:**
    - Registration/login returns a valid JWT.
    - Protected endpoints accept the token and return data (200 OK).
    - Invalid/expired tokens or missing tokens result in 403 Forbidden.

## Current Test Status

### Failing Tests

1. **JWT Authentication Tests**
   - Theatre search endpoint (`/api/theatres/movie/{movieId}/city/{city}/date/{date}`)
   - Status: 403 Forbidden
   - Issue: JWT token validation failing
   - Root Cause: Token signature mismatch

2. **Protected Endpoint Tests**
   All protected endpoints requiring authentication are failing with 403 Forbidden:
   - Theatre management endpoints
   - Movie management endpoints
   - Show management endpoints
   - Booking management endpoints

### Working Tests

1. **User Authentication**
   - Registration endpoint (`POST /api/auth/register`)
   - Login endpoint (`POST /api/auth/login`)
   - Status: Working correctly (200 OK/201 Created)

### Known Issues

1. **JWT Token Validation**
   - Token signature mismatch errors
   - Potential configuration issues with JWT secret
   - Inconsistent token generation and validation

2. **Authentication Flow**
   - Protected endpoints not accepting valid tokens
   - Authorization headers not being properly processed

### Debugging Steps

1. **JWT Configuration**
   ```sh
   # Check JWT secret configuration
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{
       "email": "testuser@example.com",
       "password": "TestPassword123"
     }'
   ```

2. **Protected Endpoint Test**
   ```sh
   # Test protected endpoint with token
   curl -X GET http://localhost:8080/api/theatres/movie/<movieId>/city/<city>/date/<date> \
     -H "Authorization: Bearer <paste-token-here>"
   ```

### Expected vs Actual Results

| Test Case | Expected | Actual | Status |
|-----------|----------|---------|---------|
| User Registration | 201 Created | 201 Created | ✅ |
| User Login | 200 OK | 200 OK | ✅ |
| Theatre Search | 200 OK | 403 Forbidden | ❌ |
| Protected Endpoints | 200 OK | 403 Forbidden | ❌ |

### Next Steps

1. Verify JWT secret consistency
2. Check token expiration configuration
3. Validate Authorization header format
4. Review user roles and permissions
5. Monitor JWT validation logs

## Testing the API

Below are the curl commands used to test the API endpoints:

### 1. Register a New User
```sh
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"theatreowner1", "password":"password123", "email":"theatreowner1@example.com", "role":"THEATRE_OWNER", "fullName":"Theatre Owner One"}'
```

### 2. Login to Get JWT Token
```sh
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"theatreowner1", "password":"password123"}'
```
Store the returned token in a variable for subsequent requests:
```sh
export TOKEN="<your_jwt_token>"
```

### 3. Create a Show
```sh
curl -X POST http://localhost:8080/api/shows \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"movieId":"1fdad558-e7c1-435d-a20b-2b6207484397", "theatreId":"cb8f7f25-4f3e-4234-ac63-8ba44e2e9dcd", "startTime":"2025-06-02T15:00:00", "endTime":"2025-06-02T17:30:00", "price":250.0, "totalSeats":100, "screenNumber":"Screen 1"}'
```

### 4. List All Shows
```sh
curl -X GET http://localhost:8080/api/shows \
  -H "Authorization: Bearer $TOKEN"
```

### 5. Check Database Directly
To check the shows table directly in the PostgreSQL database, run:
```sh
docker exec -i movie-booking-db psql -U postgres -d postgres -c "SELECT * FROM shows;"
```

## Non-Functional Requirements

### Transaction Handling
- **Atomic Seat Booking:** The system uses database transactions to ensure that seat bookings are atomic. If a booking fails (e.g., due to insufficient seats), the entire transaction is rolled back to maintain data integrity.
- **Payment Transaction Rollback:** In case of payment failures, the system automatically rolls back the booking transaction to prevent partial bookings.

### Integration Strategy
- **Existing Theatre IT Systems:** The platform provides RESTful APIs and webhooks for seamless integration with theatres' existing IT systems. This allows theatres to synchronize their inventory, schedules, and bookings in real-time.
- **New Theatre Onboarding:** New theatres can be onboarded through a self-service portal, where they can register, configure their settings, and integrate their systems using provided APIs and documentation.
- **Movie Localization:** The platform supports multi-language and multi-region content. Movies can be tagged with languages and regions, allowing users to filter and view content based on their preferences.

### Scalability & Availability
- **Multi-City and Multi-Country Scaling:** The system is designed with a microservices architecture, allowing independent scaling of components. Geographic distribution of services ensures low latency and high availability across different regions.
- **99.99% Availability Guarantee:** High availability is achieved through redundant infrastructure, load balancing, and automated failover mechanisms. Regular health checks and monitoring ensure quick detection and resolution of issues.

### Payment Gateway Integration
- **Integration with Payment Gateways:** The platform integrates with popular payment gateways such as Stripe and PayPal. This integration is handled through secure APIs, ensuring that payment processing is reliable and compliant with industry standards.

### Monetization
- **Revenue Generation:** The platform generates revenue through multiple channels:
  - **Commissions:** A percentage of each booking is taken as a commission.
  - **Advertisements:** Targeted advertisements are displayed to users based on their preferences and booking history.
  - **Subscriptions:** Premium features and services are offered through subscription plans.

### Security
- **Protection Against OWASP Top 10 Security Threats:** The platform implements security best practices to protect against common vulnerabilities, including:
  - Input validation and sanitization to prevent injection attacks.
  - Secure authentication and authorization mechanisms.
  - Regular security audits and penetration testing to identify and mitigate risks.

## Platform Provisioning, Sizing & Release Management

### Technology Choices
- **Language & Framework:** The platform is built using Java with the Spring Boot framework. This choice is justified by its robust ecosystem, strong community support, and ability to handle complex business logic efficiently.
- **Database:** PostgreSQL is used for its reliability, scalability, and support for complex queries and transactions. It is well-suited for handling the relational data model of the platform.
- **Cloud & Integration Technologies:** The platform is designed to be cloud-agnostic, allowing deployment on major cloud providers (AWS, Azure, GCP). Integration technologies include RESTful APIs and webhooks for seamless communication with external systems.

### Database & Data Modeling
- **Schema Design:** The database schema is designed to support the relationships between users, theatres, movies, shows, and bookings. It includes tables for each entity with appropriate foreign keys to maintain data integrity.
- **Transactions:** Database transactions are used to ensure atomicity, consistency, isolation, and durability (ACID) properties, especially for critical operations like seat booking and payment processing.
- **Data Relationships:** The schema includes relationships such as one-to-many (e.g., a theatre can have multiple shows) and many-to-many (e.g., users can book multiple shows).

### Enterprise Systems
- **Payments:** Integration with payment gateways like Stripe and PayPal for secure and reliable payment processing.
- **Notifications:** A notification system is implemented to alert users about booking confirmations, cancellations, and other important updates.
- **Analytics:** Analytics tools are used to gather insights on user behavior, booking patterns, and platform performance.

### Hosting & Sizing
- **Cloud/Hybrid/Multi-Cloud Hosting:** The platform is designed to be deployed on a cloud infrastructure, with options for hybrid or multi-cloud setups to ensure high availability and disaster recovery.
- **Sizing:** The infrastructure is sized based on expected load, with auto-scaling capabilities to handle peak traffic and ensure optimal performance.

### Release Management
- **Managing Releases:** Releases are managed using a CI/CD pipeline, allowing for automated testing and deployment. This ensures that updates can be rolled out quickly and reliably across different cities, languages, and theatres.

### Monitoring
- **Monitoring Solutions:** The platform uses monitoring tools to track uptime, performance, and errors. This includes real-time alerts and dashboards for quick identification and resolution of issues.

### KPIs
- **Key Performance Indicators:** KPIs for the platform include:
  - **Uptime:** Target of 99.99% availability.
  - **Response Time:** Average response time for API requests.
  - **Booking Success Rate:** Percentage of successful bookings.
  - **User Satisfaction:** Measured through user feedback and ratings.

## Presentation & Discussion Preparation

### Architecture Diagrams and Explanation
- **Architecture Overview:** The platform follows a microservices architecture, with separate services for user management, theatre management, movie management, show management, and booking management. This design allows for independent scaling and maintenance of each service.
- **Component Interaction:** Diagrams illustrate how components interact, including API gateways, service discovery, and load balancing.

### API Design and Coding Highlights
- **RESTful APIs:** The platform uses RESTful APIs for communication between services. Key endpoints include user registration, login, theatre and movie management, show creation, and booking.
- **Coding Highlights:** The codebase emphasizes clean architecture, separation of concerns, and the use of design patterns to ensure maintainability and scalability.

### Non-Functional Design Decisions
- **Scalability:** The system is designed to scale horizontally, allowing for the addition of more instances to handle increased load.
- **Security:** Security measures include JWT authentication, input validation, and protection against common vulnerabilities.
- **Performance:** Performance optimizations include caching, efficient database queries, and asynchronous processing for non-critical tasks.

### Deployment Plan
- **Deployment Strategy:** The platform is deployed using Docker containers orchestrated with Kubernetes, ensuring consistent environments across development, testing, and production.
- **CI/CD Pipeline:** Continuous Integration and Continuous Deployment (CI/CD) pipelines automate testing and deployment, reducing the risk of errors and speeding up the release process.

### Uniqueness, Extensibility, and Future Improvements
- **Uniqueness:** The platform offers a seamless booking experience with real-time seat availability and instant confirmations.
- **Extensibility:** The modular design allows for easy integration of new features and services, such as loyalty programs and advanced analytics.
- **Future Improvements:** Planned improvements include AI-driven recommendations, enhanced mobile experiences, and expanded payment options.

### High-Level Project Plan
- **Timelines:** The project is divided into phases, with initial development focusing on core features, followed by testing, deployment, and ongoing maintenance.
- **Effort Estimates:** Effort estimates are based on the complexity of each feature and the resources required for development, testing, and deployment.