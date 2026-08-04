# Employee Management System

A production-ready, enterprise Java Spring Boot 3.x application implementing a strict layered architecture:

$$\text{Controller} \longrightarrow \text{Service} \longrightarrow \text{DAO} \longrightarrow \text{Repository / Entity} \longrightarrow \text{MySQL 8}$$

---

## 🏛️ Layered Architecture & Design Pattern

```
                       HTTP Request
                            │
                            ▼
      ┌───────────────────────────────────────────┐
      │             CONTROLLER LAYER              │
      │  EmployeeController, DepartmentController │
      └─────────────────────┬─────────────────────┘
                            │ Delegates
                            ▼
      ┌───────────────────────────────────────────┐
      │              SERVICE LAYER                │
      │   EmployeeService  / EmployeeServiceImpl  │
      │ DepartmentService / DepartmentServiceImpl │
      │   (Business Validation & @Transactional)  │
      └─────────────────────┬─────────────────────┘
                            │ Delegates
                            ▼
      ┌───────────────────────────────────────────┐
      │                DAO LAYER                  │
      │     EmployeeDAO   / EmployeeDAOImpl       │
      │   DepartmentDAO  / DepartmentDAOImpl      │
      └─────────────────────┬─────────────────────┘
                            │ Calls JpaRepository / EntityManager
                            ▼
      ┌───────────────────────────────────────────┐
      │       REPOSITORY & ENTITY LAYER           │
      │   EmployeeRepository, DepartmentRepository│
      │         Employee & Department Entities    │
      └─────────────────────┬─────────────────────┘
                            │
                            ▼
      ┌───────────────────────────────────────────┐
      │             MySQL 8 DATABASE              │
      └───────────────────────────────────────────┘
```

### Exact Call Chain
$$\text{Client} \xrightarrow{\text{POST /api/employees}} \text{EmployeeController.saveEmployee(emp)}$$
$$\longrightarrow \text{EmployeeService.saveEmployee(emp)}$$
$$\longrightarrow \text{EmployeeServiceImpl.saveEmployee(emp)}$$
$$\longrightarrow \text{EmployeeDAO.save(emp)}$$
$$\longrightarrow \text{EmployeeDAOImpl.save(emp)}$$
$$\longrightarrow \text{EmployeeRepository.save(emp)} \ / \ \text{EntityManager}$$
$$\longrightarrow \text{MySQL INSERT INTO employees}$$
$$\longleftarrow \text{HTTP 201 CREATED with saved entity returned up the stack}$$

---

## 🗄️ Database Schema & Relationships

### ER Relationship
- **One-to-Many / Many-to-One (Bidirectional)**:
  - One `Department` has many `Employee`s (`@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)`).
  - Many `Employee`s belong to one `Department` (`@ManyToOne @JoinColumn(name = "dept_id")`).

```sql
CREATE DATABASE IF NOT EXISTS employee_management_db;
USE employee_management_db;

CREATE TABLE departments (
    dept_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL
);

CREATE TABLE employees (
    emp_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    salary DOUBLE NOT NULL,
    dept_id INT,
    CONSTRAINT fk_emp_dept FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
);
```

---

## 🚀 Tech Stack & Prerequisites

- **Language**: Java 17 or 21
- **Framework**: Spring Boot 3.3.4 (Spring Web, Spring Data JPA, Spring Validation)
- **ORM / Persistence**: Hibernate 6 / JPA
- **Database**: MySQL 8.x
- **Build Tool**: Apache Maven 3.8+
- **In-Memory Testing**: H2 Database (for isolated unit & integration tests)

---

## ⚙️ Configuration

Edit `src/main/resources/application.properties` to set your MySQL credentials:

```properties
spring.application.name=EmployeeManagementSystem
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/employee_management_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.open-in-view=false
```

---

## 🛠️ Building and Running

### 1. Run Automated Test Suite (38 tests)
```bash
mvn clean test
```

### 2. Run the Application
```bash
mvn spring-boot:run
```
The server will start at `http://localhost:8080`.

---

## 📡 REST API Endpoints & Sample cURL Requests

### Department Endpoints (`/api/departments`)

#### 1. Create Department
```bash
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -d '{
    "deptName": "Engineering",
    "location": "San Francisco, CA"
  }'
```
**Response (201 Created):**
```json
{
  "deptId": 1,
  "deptName": "Engineering",
  "location": "San Francisco, CA",
  "employees": []
}
```

#### 2. Get Department by ID
```bash
curl -X GET http://localhost:8080/api/departments/1
```
**Response (200 OK):**
```json
{
  "deptId": 1,
  "deptName": "Engineering",
  "location": "San Francisco, CA",
  "employees": []
}
```

#### 3. Update Department
```bash
curl -X PUT http://localhost:8080/api/departments/1 \
  -H "Content-Type: application/json" \
  -d '{
    "deptName": "Platform Engineering",
    "location": "San Francisco, CA"
  }'
```
**Response (200 OK):**
```json
{
  "deptId": 1,
  "deptName": "Platform Engineering",
  "location": "San Francisco, CA",
  "employees": []
}
```

#### 4. List All Departments
```bash
curl -X GET http://localhost:8080/api/departments
```
**Response (200 OK):**
```json
[
  {
    "deptId": 1,
    "deptName": "Platform Engineering",
    "location": "San Francisco, CA",
    "employees": []
  }
]
```

#### 5. Delete Department
```bash
curl -X DELETE http://localhost:8080/api/departments/1
```
**Response (204 No Content)**

---

### Employee Endpoints (`/api/employees`)

#### 6. Create Employee
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
**Response (201 Created):**
```json
{
  "empId": 1,
  "empName": "Alice Johnson",
  "email": "alice.johnson@example.com",
  "salary": 95000.0,
  "department": {
    "deptId": 1,
    "deptName": "Engineering",
    "location": "San Francisco, CA"
  },
  "deptId": 1
}
```

#### 7. Get Employee by ID
```bash
curl -X GET http://localhost:8080/api/employees/1
```
**Response (200 OK):**
```json
{
  "empId": 1,
  "empName": "Alice Johnson",
  "email": "alice.johnson@example.com",
  "salary": 95000.0,
  "department": {
    "deptId": 1,
    "deptName": "Engineering",
    "location": "San Francisco, CA"
  },
  "deptId": 1
}
```

#### 8. Update Employee
```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "empName": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "salary": 110000.00,
    "deptId": 1
  }'
```
**Response (200 OK):**
```json
{
  "empId": 1,
  "empName": "Alice Johnson",
  "email": "alice.johnson@example.com",
  "salary": 110000.0,
  "department": {
    "deptId": 1,
    "deptName": "Engineering",
    "location": "San Francisco, CA"
  },
  "deptId": 1
}
```

#### 9. List All Employees
```bash
curl -X GET http://localhost:8080/api/employees
```
**Response (200 OK):**
```json
[
  {
    "empId": 1,
    "empName": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "salary": 110000.0,
    "department": {
      "deptId": 1,
      "deptName": "Engineering",
      "location": "San Francisco, CA"
    },
    "deptId": 1
  }
]
```

#### 10. Delete Employee
```bash
curl -X DELETE http://localhost:8080/api/employees/1
```
**Response (204 No Content)**

---

## 🛡️ Error Handling & Validation Responses

All errors are handled by `@RestControllerAdvice` (`GlobalExceptionHandler`) and returned in a unified JSON structure:

### 1. Resource Not Found (404 NOT FOUND)
```json
{
  "timestamp": "2026-08-04 23:15:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 99",
  "path": "/api/employees/99"
}
```

### 2. Validation / Business Rule Error (400 BAD REQUEST)
```json
{
  "timestamp": "2026-08-04 23:15:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for one or more fields",
  "path": "/api/employees",
  "validationErrors": {
    "email": "Email must be a valid email format",
    "salary": "Salary must be greater than 0",
    "empName": "Employee name is required"
  }
}
```

---

## 📦 Postman Collection

Import `postman_collection.json` located at the root of the project into Postman to immediately execute all 10 preconfigured CRUD requests.
