---
title: "Supermarket Store System"
subtitle: "README"
description: "Project overview, setup, protocol, architecture, testing status, and contribution matrix for the Supermarket Store System."
module: "COMP C8Z03 Object-Oriented Programming"
stage: "2 (Group Project)"
---

# 2026 - OOP - L8 - GCA2 — Supermarket Store System

## 1. Project Overview

### Domain summary

The Supermarket Store System models two core parts of a supermarket domain: store departments and the products sold in
those departments. A department stores operational data such as its floor, zone, staffing level, budget, and whether
it requires refrigeration. A product stores catalogue and inventory information such as price, stock, sale status, and
optional discount price. The MySQL schema also includes a `department_products` bridge table to represent the
relationship between departments and products.

The project is implemented as an N-tier Java application. The client sends JSON requests over a socket connection, the
server routes each request to the correct handler, and the DAO layer performs persistence through JDBC using MySQL.
Stage 1 established the entity classes, validation, DAO interfaces, JDBC implementations, predicate-based filtering,
and JSON conversion. Stage 2 extends that work with a multithreaded server, a shared `ServerResponse<T>` wrapper, and
socket-based CRUD flows for the currently supported operations.

At the current repository state, the implemented end-to-end protocol covers read and insert for departments, and read,
insert, update, and delete for products. The README below documents the exact protocol and the actual project setup in
this repository rather than the original course template.

### Team

- **Group ID:** `2025-26-L8-OOP-GCA2-SD2a`
- **Members:**
    - Hanna Bokariuk — `D00283065`
    - Nikita Smiichyk — `D00283070`

### Key features

- Java 17 Maven project using Jackson, JUnit 5, and MySQL Connector/J
- Validated `Department` and `Product` domain models
- DAO interfaces plus JDBC implementations for both entities
- JSON conversion utilities and socket protocol DTOs
- Multithreaded socket server using `ExecutorService`
- Integration tests for DAO behaviour and unit tests for entity and JSON logic

---

## 2. How to Run

### Prerequisites

- Java: `17+` (or the version used in labs)
- IntelliJ IDEA (recommended)
- MySQL Server (local)
- Maven

### 2.1 Database setup

1. Run `sql/mysqlSetup.sql` in MySQL.
2. The script drops and recreates the database named `supermarket_store_system`.
3. The script creates these tables:
    - `departments`
    - `products`
    - `department_products`
4. Seed data is inserted for both main tables and for the bridge table assignments.

### 2.2 Configure credentials

This project currently does **not** read database settings from a config file.

Current code defaults:

- Database URL: `jdbc:mysql://localhost:3306/supermarket_store_system`
- Database user: `root`
- Database password: environment variable `TEST_DB_PASS`

Before running the server or the JDBC integration tests, set:

- `TEST_DB_PASS=<your_mysql_password>`

### 2.3 Run the server

- Main class: `com.supermarketstore.server.ServerMain`
- Default port: `9000`
- Expected output:
    - “Server listening on …”
    - Logs for client connect/disconnect

### 2.4 Run the client(s)

- Main class: `com.supermarketstore.client.ClientMain`
- The current client is a console demo that sends a fixed sequence of protocol requests.
- It demonstrates:
    - get all departments
    - get department by id
    - add department
    - get all products
    - get product by id
    - add product
- Product delete and update are implemented on the server side, but they are not yet demonstrated by the current
  client flow.

---

## 3. Architecture Summary

### 3.1 N-tier overview

- Client layer:
    - `ClientMain` serializes `ClientRequest` objects to JSON and parses `ServerResponse<T>` replies.
- Protocol layer:
    - `ClientRequest`, `RequestType`, and `ServerResponse<T>` define the shared request and response contract.
- Server layer:
    - `ServerMain` accepts socket connections and uses `ExecutorService` so each client runs on a separate thread.
    - `RequestRouter` dispatches each request type to a matching handler.
- DAO layer:
    - `DepartmentDao` / `JdbcDepartmentDao`
    - `ProductDao` / `JdbcProductDao`
- Database layer:
    - MySQL schema `supermarket_store_system`
    - tables `departments`, `products`, and `department_products`

### 3.2 Architecture diagram

- Path: `docs/architecture.md`
- Diagram format: Mermaid
- The diagram already matches the current client -> protocol -> server -> DAO -> database flow.

---

## 4. JSON Protocol Documentation

> This section documents the socket protocol currently used by the Stage 2 client and server implementation.

### 4.1 Envelope format

- **Request**
    - `type`: string enum from `RequestType`
    - `payload`: JSON object or `null`
- **Response**
    - `status`: `OK` | `ERROR`
    - `message`: human-readable result text
    - `data`: object, array, or `null`

Example request with payload:

```json
{
  "type": "GET_PRODUCT_BY_ID",
  "payload": {
    "id": 1
  }
}
```

Example request without payload:

```json
{
  "type": "GET_ALL_PRODUCTS",
  "payload": null
}
```

Example success response:

```json
{
  "status": "OK",
  "message": "Product retrieved successfully",
  "data": {
    "name": "Milk",
    "price": 2.49,
    "stock": 30,
    "product_id": 1,
    "is_on_sale": true,
    "discount_price": 1.99
  }
}
```

Example failure response:

```json
{
  "status": "ERROR",
  "message": "Missing required field: id",
  "data": null
}
```

Example department insert request:

```json
{
  "type": "ADD_DEPARTMENT",
  "payload": {
    "name": "Bakery",
    "floor": 1,
    "zone": 3,
    "budget": 25000.0,
    "employeeCount": 8,
    "isRefrigerated": false
  }
}
```

Example department success response:

```json
{
  "status": "OK",
  "message": "Department added successfully",
  "data": {
    "department_id": 11,
    "name": "Bakery",
    "floor": 1,
    "zone": 3,
    "budget": 25000.0,
    "employee_count": 8,
    "is_refrigerated": false
  }
}
```

### 4.2 Supported request types

| Request Type | Payload fields | Success response data | Failure examples |
|:--|:--|:--|:--|
| `GET_ALL_DEPARTMENTS` | none (`payload = null`) | `data` = array of department objects | DAO or server error |
| `GET_DEPARTMENT_BY_ID` | `id:int` | `data` = one department object | missing `id`, department not found |
| `ADD_DEPARTMENT` | `name:string`, `floor:int`, `zone:int`, `budget:double`, `employeeCount:int`, `isRefrigerated:boolean` | `data` = inserted department with generated `department_id` | missing fields, validation error, DAO error |
| `GET_ALL_PRODUCTS` | none (`payload = null`) | `data` = array of product objects | DAO or server error |
| `GET_PRODUCT_BY_ID` | `id:int` | `data` = one product object | missing `id`, product not found |
| `ADD_PRODUCT` | `name:string`, `price:double`, `isOnSale:boolean` or `is_on_sale:boolean`, `stock:int`, optional `discountPrice:double` or `discount_price:double` | `data` = inserted product with generated `product_id` | missing fields, missing discount price for sale item, validation error, DAO error |
| `DELETE_PRODUCT_BY_ID` | `id:int` | `data` = `null`, success confirmed by message | missing `id`, product not found |
| `UPDATE_PRODUCT` | `id:int`, `name:string`, `price:double`, `isOnSale:boolean` or `is_on_sale:boolean`, `stock:int`, optional `discountPrice:double` or `discount_price:double` | `data` = updated product object | missing fields, missing discount price for sale item, product not found, validation error, DAO error |

### 4.3 Entity JSON shapes

Department objects are serialized with these JSON keys:

```json
{
  "department_id": 1,
  "name": "Bakery",
  "floor": 1,
  "zone": 3,
  "budget": 25000.0,
  "employee_count": 8,
  "is_refrigerated": false
}
```

Product objects are serialized with these JSON keys:

```json
{
  "product_id": 1,
  "name": "Milk",
  "price": 2.49,
  "is_on_sale": true,
  "discount_price": 1.99,
  "stock": 30
}
```

### 4.4 Current protocol notes

- Requests are sent as single-line JSON messages over a TCP socket.
- Responses are always wrapped in `ServerResponse<T>`.
- Unknown request types return `status = ERROR` with message `Unknown request type: ...`.
- `DELETE_DEPARTMENT_BY_ID` and `UPDATE_DEPARTMENT` exist in `RequestType`, but their handlers are not implemented in the current router yet, so they are not listed as supported operations above.

---

## 5. Binary File Handling Status

Binary file handling is not implemented in the current repository state.

Current status:

- No BLOB columns exist in the MySQL schema.
- No binary upload or retrieval request types exist in `RequestType`.
- No client or server flow currently transfers files.

This section is kept only to document the present status of the project. If binary handling is added in a later stage,
this README should be extended with the exact schema, request types, and payload format used.

---

## 6. Testing & Coverage

### 6.1 Running tests

- Command: `mvn test`
- Test source root: `src/test/java`
- Current test classes:
    - `DepartmentTest`
    - `ProductTest`
    - `JacksonDepartmentJsonConverterTest`
    - `JacksonProductJsonConverterTest`
    - `JdbcDepartmentDaoTest`
    - `JdbcProductDaoTest`
- The JDBC integration tests use the local MySQL database and also require `TEST_DB_PASS`.

### 6.2 Current coverage status

- Coverage evidence is not committed in the current repository state.
- The existing tests currently focus on:
    - entity validation and constructor/setter rules
    - Jackson JSON round-trip behaviour
    - DAO insert, read, update, delete, and predicate-based filtering

---

## 7. Design Patterns, Generics, Lambdas

### 7.1 Patterns used (minimum 2)

- DAO pattern:
    - `DepartmentDao` and `ProductDao` separate persistence logic from the rest of the application, while
      `JdbcDepartmentDao` and `JdbcProductDao` provide the concrete JDBC implementation.
- Router / command-style dispatch:
    - `RequestRouter` maps request type strings to handler functions, so the server can dispatch incoming protocol
      messages without large conditional chains.

### 7.2 Generics usage

- `ServerResponse<T>`
- `TypeReference<ServerResponse<List<Department>>>` and similar usages in the client when parsing JSON responses

### 7.3 Functional interfaces / lambdas

- `Predicate<T>` filtering
- Lambda-based request handlers registered in `RequestRouter`

---

## 8. Screencast Status

- A screencast link is not included in the current repository state.
- This section should be updated with the final video URL before submission if the module requires it.

---

## 9. Contribution Matrix (Required)

The matrix below reflects the current project ownership visible from the codebase and the feature tracking table.

| Major task | Primary author | Contributor / reviewer | Notes |
|:--|:--|:--|:--|
| Department domain model, validation, DAO contract, and JDBC DAO | Hanna Bokariuk | Nikita Smiichyk | Based on package ownership and authorship tags |
| Product domain model, validation, DAO contract, and JDBC DAO | Nikita Smiichyk | Hanna Bokariuk | Based on package ownership and authorship tags |
| SQL schema and seed data for `departments`, `products`, and `department_products` | Shared | Shared | Central database setup used by both entity areas |
| JSON request and response envelope classes | Hanna Bokariuk | Nikita Smiichyk | `ClientRequest` authored by Hanna with Nikita contributor tag |
| Request type enum and product-side protocol constants | Nikita Smiichyk | Shared | `RequestType` currently maintained alongside product flow work |
| Socket server and multithreaded request handling | Shared | Shared | `ServerMain` and `RequestRouter` integrate both entity areas |
| Department client demo flow | Hanna Bokariuk | Shared | `runDepartmentDemo` is marked with Hanna authorship |
| Product client demo flow | Nikita Smiichyk | Shared | `runProductDemo` is marked with Nikita authorship |
| DAO integration tests and JSON/entity tests | Shared | Shared | Separate test classes exist for both department and product modules |
| Architecture and protocol documentation | Shared | Shared | Includes Mermaid architecture diagram and this README |

---

## 10. References (Harvard)

- Oracle (n.d.) *JDBC Basics*. Available at: [https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)
- FasterXML (n.d.) *Jackson Databind*. Available at: [https://github.com/FasterXML/jackson-databind](https://github.com/FasterXML/jackson-databind)
- JUnit Team (n.d.) *JUnit 5 User Guide*. Available at: [https://junit.org/junit5/docs/current/user-guide/](https://junit.org/junit5/docs/current/user-guide/)
- MySQL (n.d.) *MySQL Connector/J Developer Guide*. Available at: [https://dev.mysql.com/doc/connector-j/en/](https://dev.mysql.com/doc/connector-j/en/)

---

## 11. AI Tool Use Declaration

- AI tools were used for limited support tasks such as documentation wording, README restructuring, and example
  formatting.
- All project-specific content in this README was reviewed and adapted to match the current repository state before it
  was committed.
- Code, database schema decisions, protocol behaviour, and project verification remain the responsibility of the team.
