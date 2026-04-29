---
title: "Supermarket Store System"
subtitle: "README"
description: "Project overview, setup, protocol, architecture, binary file handling, testing status, and contribution matrix for the Supermarket Store System."
module: "COMP C8Z03 Object-Oriented Programming"
stage: "4 (Final Group Project Submission)"
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
and JSON conversion. Stage 2 extended that work with a multithreaded server, a shared `ServerResponse<T>` wrapper, and
socket-based CRUD flows. Stage 3 added binary file metadata and BLOB handling for departments and products. Stage 4
focuses on the expanded test suite, coverage evidence, final documentation, the contribution matrix, and the final
demo submission.

The project supports JSON-based read, insert, update, delete, image retrieval, and disconnect flows through the shared
client-server protocol. This README documents the actual repository state and final submission requirements rather than
the original course template.

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
- Binary image upload and retrieval support with file metadata
- Integration tests for DAO behaviour and unit tests for entity and JSON logic
- Separate project contribution matrix in `CONTRIBUTION_MATRIX.md`

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
    - update department
    - delete department
    - retrieve department image data
    - get all products
    - get product by id
    - add product
    - update product
    - delete product
    - send a structured disconnect request

---

## 3. Architecture Summary

### 3.1 N-tier overview

- Client layer:
    - `ClientMain` serialises `ClientRequest` objects to JSON and parses `ServerResponse<T>` replies.
- Protocol layer:
    - `ClientRequest`, `RequestType`, and `ServerResponse<T>` define the shared request and response contract.
- Server layer:
    - `ServerMain` accepts socket connections and uses `ExecutorService` so each client runs on a separate thread.
    - `RequestRouter` dispatches each request type to a matching handler.
- File payload layer:
    - `FilePayloadBuilder` reads local files and builds Base64 JSON payload fragments with metadata.
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

> This section documents the current socket protocol used by the client and server implementation.

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
| `GET_ALL_DEPARTMENTS` | none (`payload = null`) | `data` = array of department objects without image bytes | DAO or server error |
| `GET_DEPARTMENT_BY_ID` | `id:int` | `data` = one department object with metadata only | missing `id`, department not found |
| `GET_DEPARTMENT_IMAGE_BY_ID` | `id:int` | `data` = one department object including image bytes and metadata | missing `id`, department not found |
| `ADD_DEPARTMENT` | `name:string`, `floor:int`, `zone:int`, `budget:double`, `employeeCount:int`, `isRefrigerated:boolean`, optional `fileData` or `file_data`, `fileName` or `file_name`, `contentType` or `content_type`, `fileSize` or `file_size` | `data` = inserted department with generated `department_id` | missing fields, invalid file data, validation error, DAO error |
| `DELETE_DEPARTMENT_BY_ID` | `id:int` | `data` = `null`, success confirmed by message | missing `id`, department not found |
| `UPDATE_DEPARTMENT` | `id:int`, department fields, optional binary file payload fields | `data` = updated department object | missing fields, invalid file data, department not found, validation error, DAO error |
| `GET_ALL_PRODUCTS` | none (`payload = null`) | `data` = array of product objects without image bytes | DAO or server error |
| `GET_PRODUCT_BY_ID` | `id:int` | `data` = one product object with metadata only | missing `id`, product not found |
| `GET_PRODUCT_IMAGE_BY_ID` | `id:int` | `data` = one product object including image bytes and metadata | missing `id`, product not found |
| `ADD_PRODUCT` | `name:string`, `price:double`, `isOnSale:boolean` or `is_on_sale:boolean`, `stock:int`, optional `discountPrice:double` or `discount_price:double`, optional binary file payload fields | `data` = inserted product with generated `product_id` | missing fields, missing discount price for sale item, invalid file data, validation error, DAO error |
| `DELETE_PRODUCT_BY_ID` | `id:int` | `data` = `null`, success confirmed by message | missing `id`, product not found |
| `UPDATE_PRODUCT` | `id:int`, product fields, optional binary file payload fields | `data` = updated product object | missing fields, missing discount price for sale item, invalid file data, product not found, validation error, DAO error |
| `DISCONNECT` | none (`payload = null`) | `data` = `null`, success confirmed by message | server error |

### 4.3 Entity JSON shapes

Department objects are serialised with these JSON keys:

```json
{
  "department_id": 1,
  "name": "Bakery",
  "floor": 1,
  "zone": 3,
  "budget": 25000.0,
  "employee_count": 8,
  "is_refrigerated": false,
  "file_name": "bakery.png",
  "content_type": "image/png",
  "file_size": 1024,
  "department_image": null
}
```

Product objects are serialised with these JSON keys:

```json
{
  "product_id": 1,
  "name": "Milk",
  "price": 2.49,
  "is_on_sale": true,
  "discount_price": 1.99,
  "stock": 30,
  "file_data": null,
  "file_name": "milk.jpeg",
  "content_type": "image/jpeg",
  "file_size": 2048
}
```

### 4.4 Current protocol notes

- Requests are sent as single-line JSON messages over a TCP socket.
- Responses are always wrapped in `ServerResponse<T>`.
- Unknown request types return `status = ERROR` with message `Unknown request type: ...`.
- Missing required payload fields return `status = ERROR` with a descriptive message.
- `GET`, `ADD`, `UPDATE`, `DELETE`, and image retrieval flows are implemented for both `Department` and `Product`.
- Delete operations return `data = null` and use the response message to confirm success or explain failure.
- Binary uploads use Base64 file data plus filename, content type, and file size metadata.
- Image retrieval requests return the stored binary data and metadata for the requested record.
- The client sends `DISCONNECT` before closing the socket.
- The current console client demonstrates the protocol by sending a fixed sequence of department and product requests.


---

## 5. Binary File Handling

Binary file handling is implemented for both departments and products.

Current implementation:

- The `departments` table stores `file_name`, `content_type`, `file_size`, and `department_image`.
- The `products` table stores `file_name`, `content_type`, `file_size`, and `product_image`.
- `FilePayloadBuilder` reads a local file, Base64-encodes the bytes, and adds metadata to the JSON payload.
- `RequestRouter` accepts either `fileData` or `file_data`, decodes the Base64 content, validates the file payload, and
  passes the bytes to the DAO layer.
- `JdbcDepartmentDao` and `JdbcProductDao` store file bytes through JDBC and provide image retrieval methods.
- `GET_DEPARTMENT_IMAGE_BY_ID` and `GET_PRODUCT_IMAGE_BY_ID` return the requested entity including binary data and
  metadata.
- The department client demo writes retrieved image data to `downloads/departments/`.

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

## 9. Contribution Matrix

The full project contribution matrix is maintained in `CONTRIBUTION_MATRIX.md`.

Domain ownership:

- Hanna Bokariuk led the Department-related implementation.
- Nikita Smiichyk led the Product-related implementation.

Selected shared tasks:

| Feature / Task | Primary author | Reviewer / contributor | Notes |
|:--|:--|:--|:--|
| F10 - Multithreaded server | Nikita Smiichyk | Hanna Bokariuk | Nikita implemented the `ExecutorService` client pool and multithreaded accept loop; Hanna created the initial socket server skeleton and later updated the client loop for `DISCONNECT`. |
| F11 - `ServerResponse<T>` wrapper | Hanna Bokariuk | Nikita Smiichyk | Hanna added the initial generic wrapper; Nikita refined it with the starter-compatible structure, `OK`/`ERROR` helpers, and consistent routing usage. |
| F16 - Shared protocol structure | Nikita Smiichyk | Hanna Bokariuk | Nikita added the shared `RequestType` enum, expanded request types for CRUD, standardised routing/error handling, and refactored shared client request helpers; Hanna created the initial request/response protocol classes and added later protocol types such as `DISCONNECT`. |
| Architecture diagram | Nikita Smiichyk |  | One-page annotated architecture diagram and updates after architecture changes. |
| README | Nikita Smiichyk | Hanna Bokariuk | Setup guide, protocol documentation, architecture summary, testing evidence, and references. |

---

## 10. Required OOP Features

| Requirement | Project evidence |
|:--|:--|
| Javadoc documentation | Main classes include class-level Javadoc with author information. Non-trivial methods should continue to be documented as the project is finalised. |
| `Optional<T>` | DAO lookup methods such as `getDepartmentById`, `getProductById`, `getDepartmentImageById`, and `getProductImageById` return `Optional<T>` rather than `null`. |
| Design patterns | DAO is used for persistence abstraction. Router / command-style dispatch is used through `RequestRouter` and request handlers. |
| Generics | `ServerResponse<T>` is used for typed server replies. Jackson `TypeReference<ServerResponse<List<T>>>` is used in the client for typed parsing. |
| Functional interfaces and lambdas | `Predicate<T>` is used for entity filtering. `RequestRouter` registers lambda-based request handlers. |
| Collections | `List<T>` is used for ordered DAO result sets. Handler maps are used for request type lookup. |
| DRY principle | Shared client request helpers, response printing, and router dispatch reduce repeated request/response code. |
| Architecture diagram | `docs/architecture.md` documents the client, protocol, server, DAO, database, and binary file handling flow. |

---

## 11. Optional Technical Excellence

This optional component is not required for the base Stage 4 submission. If attempted, the implementation must provide a
generic service abstraction layer with at least two concrete service types, polymorphic service routing, JUnit tests,
README design rationale, and an updated architecture diagram.

Suitable pattern choices include Factory, Strategy, Template Method, or Command. The final demo explanation should
justify why the chosen pattern fits the service architecture.

---

## 12. References (Harvard)

- Oracle (n.d.) *JDBC Basics*. Available at: [https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)
- FasterXML (n.d.) *Jackson Databind*. Available at: [https://github.com/FasterXML/jackson-databind](https://github.com/FasterXML/jackson-databind)
- JUnit Team (n.d.) *JUnit 5 User Guide*. Available at: [https://junit.org/junit5/docs/current/user-guide/](https://junit.org/junit5/docs/current/user-guide/)
- MySQL (n.d.) *MySQL Connector/J Developer Guide*. Available at: [https://dev.mysql.com/doc/connector-j/en/](https://dev.mysql.com/doc/connector-j/en/)

---

## 13. AI Tool Use Declaration

- AI tools were used for limited support tasks such as documentation wording, README restructuring, and example
  formatting.
