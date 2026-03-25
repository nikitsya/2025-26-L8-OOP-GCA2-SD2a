---
title: "GCA2 — N-tier System"
subtitle: "README"
description: "Project overview, setup, protocol, architecture, testing evidence, and contribution matrix for GCA2."
module: "COMP C8Z03 Object-Oriented Programming"
stage: "2 (Group Project)"
---

# 2026 - OOP - L8 - GCA2 — N-tier System

## 1. Project Overview

### Domain summary (150–200 words)

> Our project domain is a Supermarket Store System that models how a modern grocery store is organised and how it
> handles both in-store stock and online orders. The supermarket is structured into Departments such as Meat, Bakery,
> Dairy, Produce, and Household. Each department contains a set of Products, and every product belongs to one
> department,
> reflecting real store layout and responsibility areas.
> In addition to managing departments and products, the system supports Online Orders. A customer order contains a list
> of requested products and quantities. When an order is received, the store can check current stock levels and
> determine
> whether the full order can be fulfilled. If items are unavailable, the order can be marked as partially fulfillable,
> with missing items clearly identified. This mirrors the real process where staff pick items from shelves and stock
> levels change constantly.
> The system provides a clear view of how departments, products, and orders connect inside a supermarket and supports
> day-to-day store operations such as maintaining inventory and processing customer orders.

### Team

- **Group ID:** `2025-26-L8-OOP-GCA2-SD2a`
- **Members:**
    - Hanna Bokariuk — `D00283065`
    - Nikita Smiichyk — `D00283070`

### Key features

- JDBC DAO layer with full CRUD (Stage 1 foundation)
- Client–server (sockets) JSON protocol + `ServerResponse<T>` wrapper
- Multithreaded server using `ExecutorService`
- Binary file upload + retrieval stored as DB BLOB with metadata
- JUnit 5 test suite with ≥70% line coverage evidence at final stage

---

## 2. How to Run

### Prerequisites

- Java: `17+` (or the version used in labs)
- IntelliJ IDEA (recommended)
- MySQL Server (local)
- Maven/Gradle (as per your project setup)

### 2.1 Database setup

1. Create a database (example): `gca2_db`
2. Run the script:
    - `sql/mysqlSetup.sql`
3. Verify seed data:
    - Each table has at least 10 rows.

### 2.2 Configure credentials

Create a local config file (do **not** commit credentials):

- `config/db.properties` (example keys)
    - `db.url=jdbc:mysql://localhost:3306/gca2_db`
    - `db.user=...`
    - `db.password=...`

### 2.3 Run the server

- Main class: `server.ServerMain`
- Default port: `9000`
- Expected output:
    - “Server listening on …”
    - Logs for client connect/disconnect

### 2.4 Run the client(s)

- Main class: `client.ClientMain`
- Run **two clients simultaneously** for Stage 2+ demonstration.

---

## 3. Architecture Summary

### 3.1 N-tier overview

- Client (UI / console)
- Server (socket listener + request handlers + threading)
- DAO layer (interfaces + JDBC implementations)
- Database (MySQL)

### 3.2 Architecture diagram

- Path: `docs/architecture.md`
- Diagram format: Mermaid (preferred)

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

## 5. Binary File Handling (Stage 3+)

### 5.1 What binary data represents in our domain

- Example: Player profile image / Evidence photo / Receipt scan / Audio clip

### 5.2 Storage approach

- DB table includes:
    - `blob_data` (BLOB)
    - `file_name` (VARCHAR)
    - `content_type` (VARCHAR)
    - `file_size` (INT)

### 5.3 Supported binary operations

| Operation           | Request type                 | Notes                                     |
|:--------------------|:-----------------------------|:------------------------------------------|
| Upload file         | `UPLOAD_<ENTITY>_FILE`       | Base64 encode bytes + include metadata    |
| Retrieve file       | `GET_<ENTITY>_FILE`          | Base64 returned, client reconstructs file |
| Query metadata only | `GET_<ENTITY>_FILE_METADATA` | Must not fetch the BLOB payload           |

---

## 6. Testing & Coverage

### 6.1 Running tests

- Command:
    - `mvn test` (or your equivalent)
- Location:
    - `src/test/java/...`

### 6.2 Coverage evidence (Stage 4)

- Coverage screenshot committed to:
    - `/reports/coverage.png`
- Target:
    - **≥ 70% line coverage** across DAO + JSON + binary handling classes

---

## 7. Design Patterns, Generics, Lambdas

### 7.1 Patterns used (minimum 2)

- Pattern 1: `<name>` — why it fits
- Pattern 2: `<name>` — why it fits

### 7.2 Generics usage

- `ServerResponse<T>`
- Any additional generic abstractions

### 7.3 Functional interfaces / lambdas

- `Predicate<T>` filtering
- Any other meaningful lambdas

---

## 8. Screencast (Stage 4)

- URL: [YouTube link](www.youtube.com)

---

## 9. Contribution Matrix (Required)

> One row per **major task**. “Primary” means who implemented first version. “Contributor/Reviewer” means meaningful
> review, refactor, debugging, extension, or pair work.

### 9.1 Matrix (example for a 3-person team)

| Major task                                                              | Primary author | Contributor / reviewer | Notes                              |
|:------------------------------------------------------------------------|:---------------|:-----------------------|:-----------------------------------|
| Domain proposal email (150–200 words) + entity list for approval        | Student A      | Student B              | Drafted + refined before sending   |
| Repo setup (private repo, collaborators, branch plan stage1–stage4)     | Student B      | Student C              | Created branches + README skeleton |
| `mysqlSetup.sql` schema + seed data (10+ rows per table)                | Student C      | Student A              | Re-runnable from scratch           |
| DTO/entity modelling + validation rules (trim/blank/range checks)       | Student A      | Student C              | Included int/double/string fields  |
| DAO interfaces (XxxDao) for all entities                                | Student B      | Student A              | Service depends on interfaces only |
| JDBC DAO implementation: `getAll` + `getById` using `Optional<T>`       | Student B      | Student C              | PreparedStatements throughout      |
| JDBC DAO implementation: `insert` returning generated keys              | Student C      | Student B              | Verified `getGeneratedKeys()`      |
| JDBC DAO implementation: `update` + `deleteById`                        | Student B      | Student A              | Consistent return semantics        |
| Predicate filtering API (`findByFilter(Predicate<T>)`)                  | Student A      | Student B              | Lambda-based filtering             |
| JSON conversion (toJson/fromJson/listToJson) per entity                 | Student A      | Student C              | Round-trip verified                |
| Architecture diagram (Mermaid) + annotated tier explanation             | Student C      | Student B              | Updated as architecture evolved    |
| Multithreaded server (`ExecutorService`, client handler per connection) | Student B      | Student C              | Clean shutdown + logging           |
| `ServerResponse<T>` wrapper + consistent response mapping               | Student B      | Student A              | No raw types                       |
| Protocol documentation in README (all request types + payloads)         | Student A      | Student B              | Kept current per stage             |
| Client features: display all + display by id                            | Student C      | Student A              | Implemented for owned entity       |
| Client features: insert/update/delete over sockets                      | Student C      | Student B              | Handles failures gracefully        |
| Error handling: structured failures (no stack traces to client)         | Student B      | Student A              | Includes validation + DB errors    |
| Binary schema extension (BLOB + metadata columns)                       | Student A      | Student C              | Updated `mysqlSetup.sql`           |
| Binary upload (Base64 encode/decode + DB storage)                       | Student A      | Student B              | Stored bytes + metadata            |
| Binary retrieval (reconstruct file on client)                           | Student A      | Student C              | Verified bytes match               |
| Metadata-only query (no BLOB fetch)                                     | Student B      | Student A              | Separate DAO method                |
| Disconnect protocol (`DISCONNECT`) + cleanup                            | Student C      | Student B              | Releases thread cleanly            |
| Stage 3 core tests (DAO read, insert+id, JSON round-trip)               | Student C      | Student A              | 3+ tests each                      |
| Stage 4 extended tests (server scenario + binary scenario + full DAO)   | Student B      | Student C              | Added 3+ more each                 |
| Coverage evidence screenshot `/reports/coverage.png`                    | Student A      | Student B              | IntelliJ coverage runner           |
| Screencast (8–10 min): demo + design iterations                         | Student C      | Student A              | Script + recording + export        |
| Harvard references + AI usage declaration                               | Student A      | Student B              | All sources cited                  |
| Final README polish (run steps, protocol, testing, evidence links)      | Student B      | Student C              | Consistent formatting              |

---

## 10. References (Harvard)

- [1] …
- [2] …

---

## 11. AI Tool Use Declaration

- Tools used:
    - …
- What was generated:
    - …
- What was modified by the team:
    - …
