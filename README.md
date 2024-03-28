# to-do-service

A backend service allowing basic management of a simple to-do list.

## Description

The to-do service provides a RESTful API for managing to-do items. Users can add new items, mark items as done, update item descriptions, and retrieve lists of items. The service automatically handles overdue items by marking them as "past due."

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
   
Depending on your in used ports, you could use a free port