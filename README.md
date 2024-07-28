# ToDoList API

This is a backend API for a ToDoList application using Spring Boot. It provides CRUD operations for managing user tasks with JWT authentication.

## Prerequisites

- Java 22
- Maven 3.6+
- PostgreSQL
- Postman (for API testing)

## Installation

Clone this repository
   ```sh
   git clone https://github.com/username/todolist-be.git
   cd todolist-be
   spring.datasource.url=jdbc:mysql://localhost:3306/todolist
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   spring.jpa.hibernate.ddl-auto=update
   jwt.secret=your_jwt_secret
   mvn clean install
   mvn spring-boot:run

   
