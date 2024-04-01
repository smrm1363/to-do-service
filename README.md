# To-do-service

A backend service allowing basic management of a simple to-do list.

## Description

The to-do service provides a RESTful API for managing to-do items. Users can add new items, mark items as done, update item descriptions, and retrieve lists of items. The service automatically handles overdue items by marking them as "past due."

This is a sample project demonstrating basic functionality, with unit and integration tests included. While the test coverage is not extensive, it provides a foundation for further development.

### Architecture

The project uses a Hexagonal architecture to ensure separation of concerns and maintainability.

### Tech stack

- Java 17
- Spring Boot
- H2 Database
- REST API
- JPA
- Docker
- Gradle

## How to Run

### Prerequisites

- Java Development Kit (JDK) installed
- Docker installed

### Build and Run

1. **Build the Project:**

   ```bash
   ./gradlew build

2. **Build the Docker image:**

   ```bash
   ./gradlew bootBuildImage
3. **Run the Docker image:**

   ```bash
   docker run -p 8089:8080 docker.io/library/to-do-service:0.0.1
   
Replace 8089 with a free port if needed.

4.  **How to Run Tests**

      Use the following command to run all tests:

      ```bash
      ./gradlew test
    
#### Open-API/Swagger UI
You can access Open-API/Swagger UI documentations via following links:

http://localhost:8080/swagger-ui/index.html

http://localhost:8080/v3/api-docs

http://localhost:8080/v3/api-docs..yaml


## Additional Notes

Indeed, this is just a sample project and not a very comprehensive one. While it provides basic functionality for managing to-do items, there are several areas where it could be improved and expanded:

- **Test Coverage:** While there are basic unit and integration tests, the test coverage is not extensive. Additional tests could be added to cover more edge cases and scenarios.
- **Error Handling:** The error handling in the application could be enhanced to provide more informative error messages and handle a wider range of potential errors.
- **Security:** Security measures such as authentication and authorization could be implemented to secure the API endpoints and prevent unauthorized access.
- **Logging:** More sophisticated logging mechanisms could be implemented to provide better visibility into the application's behavior and facilitate troubleshooting.
- **Performance Optimization:** The application's performance could be optimized further, especially for operations involving large datasets or frequent database interactions.

Despite these potential areas for improvement, the project serves as a demonstration of basic Spring Boot application development and REST API implementation.
