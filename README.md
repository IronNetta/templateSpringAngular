# Template Spring Angular Project

This project is a full-stack application template, featuring a Spring Boot backend and an Angular frontend, orchestrated using Docker Compose.

## Features

*   **Spring Boot Backend:** Robust and scalable backend built with Java and Spring Boot.
*   **Angular Frontend:** Modern and responsive user interface developed with Angular.
*   **PrimeNG Styling:** Elegant and modern UI components for the frontend.
*   **Docker Compose:** Easy setup and deployment of both backend and frontend services.

## Technologies Used

### Backend (bend)

*   Java
*   Spring Boot
*   Maven
*   Docker

### Frontend (fend)

*   Angular
*   TypeScript
*   SCSS
*   PrimeNG (UI Component Library)
*   Docker

### Other

*   Docker Compose

## Prerequisites

Before you begin, ensure you have the following installed:

*   [Git](https://git-scm.com/)
*   [Docker Desktop](https://www.docker.com/products/docker-desktop) (includes Docker Engine and Docker Compose)
*   [Node.js](https://nodejs.org/en/) (LTS version recommended) and [npm](https://www.npmjs.com/) (for frontend development)
*   [Java Development Kit (JDK) 17 or higher](https://www.oracle.com/java/technologies/downloads/) (for backend development)
*   [Maven](https://maven.apache.org/download.cgi) (for backend development)

## Getting Started

Follow these steps to get your development environment running.

### 1. Clone the Repository

```bash
git clone <repository_url>
cd templateSpringAngular
```

### 2. Build and Run with Docker Compose (Recommended)

The easiest way to get the entire application up and running is by using Docker Compose.

```bash
docker-compose up --build
```

This command will:
*   Build the Docker images for both the backend and frontend.
*   Start the backend service (Spring Boot).
*   Start the frontend service (Angular).

Once the services are up, you can access the frontend application at `http://localhost:80` (or the port configured in `docker-compose.yml`).

### 3. Manual Setup (Alternative)

If you prefer to run the backend and frontend separately without Docker Compose, follow these steps:

#### Backend Setup

1.  Navigate to the backend directory:
    ```bash
    cd bend
    ```
2.  Build the Spring Boot application:
    ```bash
    mvn clean install
    ```
3.  Run the application:
    ```bash
    mvn spring-boot:run
    ```
    The backend will typically run on `http://localhost:8080`.

#### Frontend Setup

1.  Navigate to the frontend directory:
    ```bash
    cd fend
    ```
2.  Install npm dependencies:
    ```bash
    npm install
    ```
3.  Run the Angular development server:
    ```bash
    ng serve --open
    ```
    The frontend will typically open in your browser at `http://localhost:4200`.

## Project Structure

*   `bend/`: Contains the Spring Boot backend application.
*   `fend/`: Contains the Angular frontend application.
*   `docker-compose.yml`: Defines the services for Docker Compose orchestration.

## Styling

The frontend is styled using [PrimeNG](https://primeng.org/), a rich set of UI components for Angular. The `Aura` theme is configured to provide a modern and elegant look and feel.

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues.

## License

This project is licensed under the MIT License - see the LICENSE file for details. (Note: A LICENSE file is not included in this template, but it's good practice to add one.)