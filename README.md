# Client Migration Tool

A full-stack application for migrating clients from a legacy system to a new client entity. The tool provides a web interface to view legacy clients and migrate them to the new system. During migration, client data is automatically forwarded to the new product API.

## Features

- View legacy clients that need to be migrated
- View already migrated clients
- Migrate individual clients with a single click
- Automatic forwarding of client data to new product API during migration
- Real-time updates after migration
- Visual feedback with snackbar notifications (success/error)
- Responsive Material Design UI built with Vuetify
- RESTful API for client management
- Error handling and validation
- H2 in-memory database for data persistence
- Configurable external API integration

## Tech Stack

### Backend
- **Java 21**
- **Spring Boot 4.0.1**
- **Spring Web MVC**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **RestTemplate** (for external API calls)
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

## Migration Flow

When a client is migrated:

1. The system validates that the client exists and hasn't been migrated yet
2. Client data is forwarded to the new product API endpoint (if enabled)
3. If the external API call succeeds, the client is marked as migrated in the database
4. If the external API call fails, the migration is rolled back and the client remains unmigrated
5. Visual feedback is shown to the user via snackbar notifications

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
Migrates a client from the legacy system to the new system. This endpoint:
- Validates the client exists and isn't already migrated
- Calls the external new product API to forward client data
- Marks the client as migrated in the database (only if external call succeeds)

**Parameters:**
- `id` (path parameter): The ID of the client to migrate

**Request Body:** None

**Response:**
- `200 OK` - Migration successful
- `404 Not Found` - Client not found
- `400 Bad Request` - Client already migrated or migration failed

## Project Structure

```
migration-tool/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/client/migration/
│   │   │   │       ├── config/
│   │   │   │       │   ├── MigrationProperties.java
│   │   │   │       │   └── RestTemplateConfig.java
│   │   │   │       ├── controller/
│   │   │   │       │   └── ClientController.java
│   │   │   │       ├── dto/
│   │   │   │       │   └── ClientMigrationRequest.java
│   │   │   │       ├── exception/
│   │   │   │       │   └── GlobalExceptionHandler.java
│   │   │   │       ├── model/
│   │   │   │       │   └── Client.java
│   │   │   │       ├── repository/
│   │   │   │       │   └── ClientRepository.java
│   │   │   │       ├── service/
│   │   │   │       │   ├── ClientService.java
│   │   │   │       │   └── ExternalProductService.java
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

The backend uses Spring Boot with H2 in-memory database for data persistence. Client data is stored in the database and persists during the application lifecycle.

Key components:
- **ClientController**: REST API endpoints
- **ClientService**: Business logic for client migration, orchestrates the migration process
- **ExternalProductService**: Handles HTTP calls to the new product API
- **ClientRepository**: Spring Data JPA repository for database operations
- **GlobalExceptionHandler**: Centralized error handling
- **Client**: JPA entity representing a client
- **MigrationProperties**: Configuration properties for external API integration
- **ClientMigrationRequest**: DTO for sending client data to external API

### Frontend Development

The frontend uses Vue 3 with Vuetify for UI components. It provides a responsive interface with real-time feedback via snackbar notifications.

Key components:
- **App.vue**: Main application component with snackbar notifications
- **ClientTable.vue**: Reusable table component for displaying clients with migration actions
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
  datasource:
    url: jdbc:h2:mem:migrationdb
    driverClassName: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8080

migration:
  new-product-api-url: ${NEW_PRODUCT_API_URL:http://localhost:8081/api/clients}
  enable-external-migration: ${ENABLE_EXTERNAL_MIGRATION:true}
  timeout-seconds: ${MIGRATION_TIMEOUT_SECONDS:30}

logging:
  level:
    com.client.migration: DEBUG
```

### Migration Configuration

The migration tool can be configured via environment variables or `application.yaml`:

- **`migration.new-product-api-url`**: URL of the new product API endpoint (default: `http://localhost:8081/api/clients`)
- **`migration.enable-external-migration`**: Enable/disable external API calls (default: `true`)
- **`migration.timeout-seconds`**: Request timeout in seconds (default: `30`)

**Environment Variables:**
- `NEW_PRODUCT_API_URL`: Override the new product API URL
- `ENABLE_EXTERNAL_MIGRATION`: Set to `false` to disable external migration calls
- `MIGRATION_TIMEOUT_SECONDS`: Override the timeout value

### H2 Database Console

The H2 console is enabled for development purposes. Access it at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:migrationdb`
- Username: `sa`
- Password: (empty)

### Frontend Configuration

The frontend uses Vite with proxy configuration in `frontend/vite.config.js` to forward API requests to the backend.

## Error Handling

- **Client not found**: Returns 404 with error message
- **Client already migrated**: Returns 400 with error message
- **External API failure**: Migration is rolled back, returns 500 with error message
- **Network errors**: Properly logged and handled with user-friendly error messages

All errors are displayed to users via snackbar notifications in the frontend.

## License

This project is an internal tool for client migration purposes.
