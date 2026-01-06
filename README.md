# Client Migration Tool

A full-stack application for migrating clients from a legacy system to a new client entity. The tool provides a web interface to view legacy clients and migrate them to the new system.

## Features

- View legacy clients that need to be migrated
- View already migrated clients
- Migrate individual clients with a single click
- Real-time updates after migration
- Responsive Material Design UI built with Vuetify
- RESTful API for client management
- Error handling and validation

## Tech Stack

### Backend
- **Java 21**
- **Spring Boot 4.0.1**
- **Spring Web MVC**
- **Lombok**
- **Gradle**

### Frontend
- **Vue.js 3**
- **Vuetify 3** (Material Design components)
- **Axios** (HTTP client)
- **Vite** (Build tool)
- **Material Design Icons**

## Prerequisites

- **Java 21** or higher
- **Node.js** 18+ and npm
- **Gradle** (wrapper included)

## Installation

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build the project:
```bash
./gradlew build
```

3. Run the application:
```bash
./gradlew bootRun
```

The backend will start on `http://localhost:8080`

### Frontend Setup

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
npm run dev
```

The frontend will start on `http://localhost:3000`

## Running the Application

1. Start the backend server first (port 8080)
2. Start the frontend development server (port 3000)
3. Open your browser and navigate to `http://localhost:3000`

The frontend is configured to proxy API requests to the backend automatically.

## API Endpoints

### Get Legacy Clients
```
GET /api/legacy-clients
```
Returns a list of clients that haven't been migrated yet.

**Response:**
```json
[
  {
    "id": 1,
    "name": "Client A",
    "migrated": false
  }
]
```

### Get Migrated Clients
```
GET /api/new-clients
```
Returns a list of clients that have been migrated.

**Response:**
```json
[
  {
    "id": 1,
    "name": "Client A",
    "migrated": true
  }
]
```

### Migrate Client
```
POST /api/migrate/{id}
```
Migrates a client from the legacy system to the new system.

**Parameters:**
- `id` (path parameter): The ID of the client to migrate

**Response:**
- `200 OK` - Migration successful
- `404 Not Found` - Client not found
- `400 Bad Request` - Client already migrated

## Project Structure

```
migration-tool/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/client/migration/
│   │   │   │       ├── controller/
│   │   │   │       │   └── ClientController.java
│   │   │   │       ├── exception/
│   │   │   │       │   └── GlobalExceptionHandler.java
│   │   │   │       ├── model/
│   │   │   │       │   └── Client.java
│   │   │   │       ├── service/
│   │   │   │       │   └── ClientService.java
│   │   │   │       └── MigrationApplication.java
│   │   │   └── resources/
│   │   │       └── application.yaml
│   │   └── test/
│   └── build.gradle
└── frontend/
    ├── src/
    │   ├── components/
    │   │   └── ClientTable.vue
    │   ├── plugins/
    │   │   └── vuetify.js
    │   ├── services/
    │   │   └── api.js
    │   ├── App.vue
    │   └── main.js
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## Development

### Backend Development

The backend uses Spring Boot with an in-memory data structure for demonstration purposes. In a production environment, you would replace this with a database repository.

Key components:
- **ClientController**: REST API endpoints
- **ClientService**: Business logic for client migration
- **GlobalExceptionHandler**: Centralized error handling
- **Client**: Data model representing a client entity

### Frontend Development

The frontend uses Vue 3 with Composition API patterns and Vuetify for UI components.

Key components:
- **App.vue**: Main application component
- **ClientTable.vue**: Reusable table component for displaying clients
- **api.js**: API service layer for backend communication

### Building for Production

**Backend:**
```bash
cd backend
./gradlew build
```

The JAR file will be created in `backend/build/libs/`

**Frontend:**
```bash
cd frontend
npm run build
```

The production build will be in `frontend/dist/`

## Configuration

### Backend Configuration

The backend configuration is in `backend/src/main/resources/application.yaml`:

```yaml
spring:
  application:
    name: migration

server:
  port: 8080

logging:
  level:
    com.client.migration: DEBUG
```

### Frontend Configuration

The frontend uses Vite with proxy configuration in `frontend/vite.config.js` to forward API requests to the backend.

## License

This project is a demo application for client migration purposes.
