# Supermarket Store System

## Project Stage Plan

| Stage | Week  | Deadline          | Focus                                                      | Features | Status                      | Weight |
|-------|-------|-------------------|------------------------------------------------------------|----------|-----------------------------|--------|
| 1     | Wk 6  | Sunday 8th March  | DAO layer, full CRUD, JSON conversion                      | F1-F9    | Mandatory gate - not graded | -      |
| 2     | Wk 8  | Sunday 25th March | Client-server integration, all CRUD over sockets           | F10-F16  | Graded                      | 50%    |
| 3     | Wk 11 | Sunday 26th April | Binary file handling, protocol completion, core unit tests | F17-F22  | Mandatory gate - not graded | -      |
| 4     | Wk 12 | Sunday 8th May    | Full test suite with coverage, all features stable         | F23-F24  | Graded                      | 50%    |

<details>
<summary><strong>Pre-Stage Requirements</strong></summary>

| Requirement    | Details                                                                                                                               | Done |
|----------------|---------------------------------------------------------------------------------------------------------------------------------------|------|
| Primary key    | A field named `tableName_id` (for example, `player_id`) mapped to an `INT AUTO_INCREMENT` primary key.                                | ✅    |
| Minimum fields | At least one `int`, one `double`, and one `String` field per entity.                                                                  | ✅    |
| Encapsulation  | Use private fields; validate in setters/constructors (trim input, reject blank values, enforce numeric ranges).                       | ✅    |
| Invalid data   | Detect, log, and skip bad inputs. Do not crash on expected validation errors.                                                         | ✅    |
| Seed data      | Provide a `mysqlSetup.sql` file that creates and populates each table with at least 10 rows and can recreate the schema from scratch. | ✅    |
| Ownership      | Each team member owns at least one database table and implements the full vertical slice (`DAO -> server -> GUI`) for that table.     | ✅    |

</details>

<details>
<summary><strong>Stage 1 (F1-F9)</strong></summary>

| #  | Feature                               | Specification                                                                                                                                                                              | Hanna | Nikita | 
|----|---------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------|--------|
| F1 | Entity and Database Setup             | Define a DTO class for each entity with encapsulated, validated fields. Create a `mysqlSetup.sql` file that recreates the schema and populates at least 10 rows of seed data from scratch. | ✅     | ✅      |
| F2 | DAO Interface and JDBC Implementation | Define an `XxxDao` interface and implement it in `JdbcXxxDao`. The service layer depends on the interface only. Use `PreparedStatement` throughout (no SQL string concatenation).          | ✅     | ✅      |
| F3 | Get All Entities                      | `getAllXxx()` returns a `List<T>` of all records.                                                                                                                                          | ✅     | ✅      |
| F4 | Get by ID                             | `getXxxById(int id)` returns `Optional<T>` when found, or `Optional.empty()` when not found. Never return `null`.                                                                          | ✅     | ✅      |
| F5 | Delete by ID                          | `deleteXxxById(int id)` removes the record and returns a `boolean` indicating success.                                                                                                     | ✅     | ✅      |
| F6 | Insert Entity                         | `insertXxx(T entity)` inserts a new record and returns the populated DTO including the auto-generated ID from `getGeneratedKeys()`.                                                        | ✅     | ✅      |
| F7 | Update Entity                         | `updateXxx(int id, T entity)` applies field updates and returns the updated DTO.                                                                                                           | ✅     | ✅      |
| F8 | Filter with Predicate                 | `findXxxByFilter(Predicate<T> filter)` returns a `List<T>` using a lambda or method reference (not raw SQL per filter).                                                                    | ✅     | ✅      |
| F9 | JSON Conversion                       | `xxxToJson(T entity)`, `xxxFromJson(String json)`, and `xxxListToJson(List<T> list)` are correct and round-trip verified.                                                                  | ✅     | ✅      |
| -  | Architecture Diagram                  | One-page annotated diagram showing `Client -> Server -> DAO -> Database` and the JSON protocol layer. Committed before the Stage 1 deadline (Mermaid is acceptable).                       | ✅     | ✅      |

</details>

<details>
<summary><strong>Stage 2 (F10-F16)</strong></summary>

| #   | Feature                       | Specification                                                                                                                                                                                  | Hanna | Nikita |
|-----|-------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------|--------|
| F10 | Multithreaded Server          | Server uses ExecutorService to handle each connected client on a separate thread. Server does not block on a single client.                                                                    |   ✅   |    ✅    |
| F11 | ServerResponse\<T\> Wrapper   | All server replies use ServerResponse\<T\> carrying status, message, and data. Raw types not used.                                                                                             |   ✅  |   ✅     |
| F12 | Display by ID and Display All | Client sends a JSON request; server calls the DAO and returns the result as ServerResponse\<T\> JSON; client parses and displays. Each team member implements this for their own entity.       |    ✅ |   ✅     |
| F13 | Add Entity                    | Client serialises entity data to JSON and sends an insert request. Server returns the new entity (including auto-generated ID) on success, or a structured error response on failure.          |   ✅  |   ✅     |
| F14 | Delete Entity                 | Client sends a delete request with the target ID. Server calls deleteXxxById() and returns a structured success or failure response.                                                           |   ✅   |    ✅    |
| F15 | Update Entity                 | Client sends updated field data. Server calls updateXxx() and returns the updated entity.                                                                                                      |   ✅   |    ✅    |
| F16 | Error Handling and Protocol   | Structured error responses returned for all failure cases - exceptions are not propagated to the client. Protocol documented in README: each request type, payload fields, and response shape. |   ✅   |    ✅    |

</details>

<details>
<summary><strong>Stage 3 (F17-F22)</strong></summary>

| #   | Feature                   | Specification                                                                                                                                                                                                                                                                          | Hanna | Nikita |
|-----|---------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------|--------|
| F17 | Binary Schema Extension   | Extend at least one entity table with a BLOB column and associated metadata columns: `file_name VARCHAR`, `content_type VARCHAR`, `file_size INT`. Update `sql/mysqlSetup.sql` to recreate this schema. The entity DTO must include a `byte[]` field for the binary data and corresponding metadata fields. | ✅   | ✅    |
| F18 | Binary File Upload        | Client reads a binary file from disk, Base64-encodes it, and includes it in a JSON upload request alongside the metadata fields. Server decodes the payload and stores the binary data in the database using `PreparedStatement.setBytes()` or `setBinaryStream()`. Server returns a `ServerResponse<T>` confirming the stored record including its auto-generated ID. |   ✅  |  ✅   |
| F19 | Binary File Retrieval     | Client sends a retrieval request specifying a record ID. Server fetches the BLOB using `getBytes()` or `getBinaryStream()`, Base64-encodes the data, and returns it in a `ServerResponse<T>`. Client decodes the payload and reconstructs the file on disk, preserving the original filename and extension. |   ✅  |  ✅   |
| F20 | File Metadata Query       | Client can request metadata (`filename`, `content type`, `file size`) for a stored record without downloading the full binary payload. Server returns a `ServerResponse<T>` containing metadata only - the BLOB column is not fetched for this request type.                                                            |  ✅   |  ✅   |
| F21 | Disconnect / Exit         | Client sends a structured `DISCONNECT` request before closing the socket. Server logs the disconnection and releases the thread cleanly.                                                                                                                                             | ✅   |  ✅   |
| F22 | Core Unit Tests           | A JUnit 5 test suite with at least 3 meaningful tests per team member. Tests must be in the codebase and passing. Required categories: (1) a DAO read method (`getAll` or `getById`); (2) an insert with the returned auto-generated ID verified; (3) a JSON serialisation/deserialisation round-trip. Each test must have a descriptive method name (for example, `getPlayerById_returnsEmptyOptional_whenIdDoesNotExist`). Tests must not depend on execution order; use `@BeforeEach` with known data. Coverage threshold is not required at this stage - that is assessed at Stage 4. |  ✅   |      |

</details>

<details>
<summary><strong>Stage 4 (F23-F24)</strong></summary>

### Overview

Stage 4 extends the Stage 3 unit test suite to cover all implemented features across the system and introduces a
coverage threshold. Where Stage 3 required core tests for the DAO layer and JSON conversion, Stage 4 requires tests for
every significant layer: DAO, JSON conversion, server-side request handling, and binary file upload/retrieval.

The coverage threshold of `>=70%` applies to this expanded suite and must be evidenced using the IntelliJ IDEA coverage
runner. A screenshot of the coverage panel must be committed to `reports/coverage.png`.

### Optional Technical Excellence Component

Students may optionally implement a generic service architecture worth 20 marks. This architecture should abstract the
service layer and support multiple service types through polymorphism. This is an advanced architectural challenge for
students seeking to demonstrate excellence. See Section 15 of the assignment brief for the full specification.

### Required Features

| #   | Feature             | Specification                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | Hanna | Nikita |
|-----|---------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------|--------|
| F23 | Extended Test Suite | Expand the Stage 3 test suite to cover all implemented features. Tests must cover all DAO methods (`getAll`, `getById`, `insert`, `update`, `delete`, `filter`), JSON conversion round-trips, at least one server request/response scenario using a test client or mocked input, and at least one binary file upload and retrieval scenario where a known file is uploaded, retrieved, and checked byte-for-byte. All tests must pass. Minimum 3 additional tests per team member beyond the Stage 3 baseline. |       |        |
| F24 | Coverage Threshold  | Demonstrate `>=70%` line coverage across the DAO, JSON conversion, and binary file handling classes using the IntelliJ IDEA built-in coverage runner. The committed evidence must be `reports/coverage.png`. Coverage must reflect the full test suite, not a filtered subset of classes.                                                                                                                                                                                         |       |        |

### Final Submission Items

| Item                | Requirement                                                                                                                                                                                                                                                                                                | Hanna | Nikita |
|---------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------|--------|
| Screencast          | Record an 8-10 minute screencast as specified in Section 13. Filename: `2025-26-L8-OOP-GCA2-GroupID`.                                                                                                                                                                                                      |       |        |
| Contribution Matrix | Submit the contribution matrix as described in Section 12.                                                                                                                                                                                                                                                 |       |        |
| README              | Complete all sections, including the domain overview, how to run, architecture summary, protocol documentation, design pattern justification, test coverage evidence, binary file handling description, and Harvard references. Use the sample README in Moodle as the formatting reference.                  |       |        |

</details>
