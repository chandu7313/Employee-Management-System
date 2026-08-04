# Employee Management System

Spring Boot RESTful application for managing employees and departments, implemented with a layered architecture pattern:
`Controller` -> `Service` -> `DAO` -> `Repository` -> `MySQL`.

## Architecture Overview

- **Controller Layer**: Exposes REST endpoints, validates request payloads, and handles HTTP status codes.
- **Service Layer**: Implements business logic and transaction boundaries (`@Transactional`).
- **DAO Layer**: Encapsulates persistence logic and delegates to Spring Data repositories/EntityManager.
- **Repository Layer**: Extends `JpaRepository` for CRUD and custom query operations.
- **Database**: MySQL 8 (H2 for automated testing).

## Data Model

- **Department** (`departments`): `dept_id` (PK, auto-increment), `dept_name`, `location`.
- **Employee** (`employees`): `emp_id` (PK, auto-increment), `emp_name`, `email` (unique), `salary`, `dept_id` (FK referencing `departments.dept_id`).

Relationship: One-to-Many / Many-to-One between `Department` and `Employee`.

## Requirements

- Java 17 or higher
- Maven 3.8+
- MySQL 8.x

## Database Setup

Create the MySQL database:

```sql
CREATE DATABASE IF NOT EXISTS employee_management_db;
USE employee_management_db;

CREATE TABLE IF NOT EXISTS departments (
    dept_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS employees (
    emp_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    salary DOUBLE NOT NULL,
    dept_id INT,
    CONSTRAINT fk_emp_dept FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
);
```

Configure credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Running the Application

Build and run tests:
```bash
mvn clean test
```

Start the application:
```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

## API Endpoints

### Departments (`/api/departments`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/departments` | Create a new department |
| `GET` | `/api/departments/{id}` | Get department by ID |
| `PUT` | `/api/departments/{id}` | Update department |
| `DELETE` | `/api/departments/{id}` | Delete department |
| `GET` | `/api/departments` | List all departments |

#### Example: Create Department
```bash
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -d '{
    "deptName": "Engineering",
    "location": "San Francisco"
  }'
```

### Employees (`/api/employees`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/employees` | Create a new employee |
| `GET` | `/api/employees/{id}` | Get employee by ID |
| `PUT` | `/api/employees/{id}` | Update employee |
| `DELETE` | `/api/employees/{id}` | Delete employee |
| `GET` | `/api/employees` | List all employees |

#### Example: Create Employee
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "empName": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "salary": 95000.00,
    "deptId": 1
  }'
```

## Postman Collection

Import `postman_collection.json` to test all endpoints.