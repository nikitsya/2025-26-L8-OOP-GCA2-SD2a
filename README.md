# Supermarket Store System

<details>
<summary><strong>Project Stage Plan</strong></summary>

| Stage | Week  | Deadline          | Focus                                                      | Features | Status                      | Weight |
|-------|-------|-------------------|------------------------------------------------------------|----------|-----------------------------|--------|
| 1     | Wk 6  | Sunday 8th March  | DAO layer, full CRUD, JSON conversion                      | F1-F9    | Mandatory gate - not graded | -      |
| 2     | Wk 8  | Sunday 25th March | Client-server integration, all CRUD over sockets           | F10-F16  | Graded                      | 50%    |
| 3     | Wk 11 | Sunday 26th April | Binary file handling, protocol completion, core unit tests | F17-F22  | Mandatory gate - not graded | -      |
| 4     | Wk 12 | Sunday 8th May    | Full test suite with coverage, all features stable         | F23-F24  | Graded                      | 50%    |

</details>

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

#### F19 Implementation Notes

Binary file retrieval is implemented for both departments and products. The client sends a request with the target `id`
using `GET_DEPARTMENT_IMAGE_BY_ID` or `GET_PRODUCT_IMAGE_BY_ID`. The server routes the request to the relevant DAO
method, which fetches the BLOB column with `ResultSet.getBytes()` and returns the entity inside `ServerResponse<T>`.
Jackson serialises the returned `byte[]` as Base64 in the JSON response and deserialises it back into a `byte[]` on the
client. The client then writes the bytes to `downloads/departments/` or `downloads/products/` using the stored
`file_name`, preserving the original filename and extension.

</details>

## <strong>Stage 4 (F23-F24)</strong>

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
| Screencast          | Record an 8-10 minute screencast as specified in Section 13. Filename: `2025-26-L8-OOP-GCA2-SD2a`.                                                                                                                                                                                                      |       |        |
| Contribution Matrix | Submit the contribution matrix as described in Section 12.                                                                                                                                                                                                                                                 |       |        |
| README              | Complete all sections, including the domain overview, how to run, architecture summary, protocol documentation, design pattern justification, test coverage evidence, binary file handling description, and Harvard references. Use the sample README in Moodle as the formatting reference.                  |       |        |

</details>

<details>
<summary><strong>Unit Testing Requirements</strong></summary>

Unit testing is split across two stages. Stage 3 requires a core test suite as a gate condition, while Stage 4 extends
that suite to all implemented features and adds a coverage threshold.

| Stage | Requirement | Detail | Hanna | Nikita |
|-------|-------------|--------|-------|--------|
| Stage 3 - Core Tests | Minimum test count | At least 3 meaningful JUnit 5 tests per team member. Tests must be in the codebase and passing, not only described in a document. |  |  |
| Stage 3 - Core Tests | Required categories | Tests must cover: (1) a `getAll` or `getById` DAO method; (2) an insert with the returned auto-generated ID verified; (3) a JSON serialisation/deserialisation round trip. |  |  |
| Stage 3 - Core Tests | Test quality | Tests must assert correct behaviour, not merely call methods. Each test must have a descriptive name, for example `getPlayerById_returnsEmptyOptional_whenIdDoesNotExist`. |  |  |
| Stage 3 - Core Tests | Test independence | Tests must not depend on execution order. Use `@BeforeEach` with known test data. Database tests must not corrupt production data; use a dedicated test schema or rollback strategy. |  |  |
| Stage 3 - Core Tests | Coverage | Coverage is not required at Stage 3. Coverage is assessed at Stage 4. |  |  |
| Stage 4 - Full Suite with Coverage | Extended scope | Expand the Stage 3 tests to cover all implemented features: all DAO methods, JSON conversion, at least one server request/response scenario, and at least one binary file upload/retrieval scenario where a known file is uploaded, retrieved, and checked byte-for-byte. Add at least 3 additional tests per team member beyond the Stage 3 baseline. |  |  |
| Stage 4 - Full Suite with Coverage | Coverage threshold | Achieve `>=70%` line coverage across the DAO, JSON conversion, and binary file handling classes, measured using the IntelliJ IDEA built-in coverage runner. |  |  |
| Stage 4 - Full Suite with Coverage | Coverage evidence | Commit a screenshot of the Coverage panel to `reports/coverage.png`. Coverage must reflect the full suite, not a filtered subset of classes. |  |  |
| Stage 4 - Full Suite with Coverage | All tests passing | The full suite must pass at the time of Stage 4 submission. Commented-out or assertion-free tests do not contribute to coverage or marks. |  |  |

### Running Coverage in IntelliJ IDEA

1. Right-click the test class or test folder.
2. Select `Run '<TestName>' with Coverage`.
3. Check the Coverage panel for line percentage per class.
4. Save a screenshot of the panel as `reports/coverage.png`.

</details>

<details>
<summary><strong>Optional Technical Excellence (20 Marks)</strong></summary>

This component is entirely optional. Students who complete only the base requirements can achieve 30/50 in Stage 4.
This component is for students seeking to demonstrate advanced architectural understanding.

### Objective

Design and implement a generic service abstraction layer that allows the server to handle multiple service types
polymorphically.

### Requirements

| Area | Requirement |
|------|-------------|
| Service Abstraction | Define a `Service` interface or abstract class that establishes the contract for all services. It must include methods for service execution, validation, and result handling. The design must be extensible, so adding a new service type should not require modifying existing service code. |
| Multiple Concrete Services | Implement at least two distinct, fully functional, and tested service types. Examples include `FileUploadService` with `TaskProcessingService`, `FileUploadService` with `ReportGenerationService`, or `FileUploadService` with another domain-relevant service. Each service must encapsulate its own logic and data requirements. |
| Polymorphic Service Routing | The server must accept a service request, identify the requested service type, instantiate the appropriate concrete service, and execute it polymorphically. The client specifies the service type in the request, for example `"serviceType": "FILE_UPLOAD"` or `"serviceType": "TASK_PROCESS"`. The server must use polymorphism rather than conditional branching to execute the service. |
| Design Pattern Application | Apply at least one advanced pattern appropriately. Suitable patterns include Strategy, Factory, Template Method, and Command. The pattern choice must be justified in the README and explained during the demo. |
| Testing and Documentation | Both services must be tested in the JUnit suite. The README must include a dedicated section explaining the service architecture, pattern choices, and design rationale. The architecture diagram must be updated to show the service abstraction layer. |

### Assessment Focus

Students will be assessed on design quality, pattern application, polymorphic implementation, functionality, and their
ability to explain the architectural decisions during the demo. See Appendix A.2 of the assignment brief for the full
component rubric.

</details>

<details>
<summary><strong>Required OOP Features</strong></summary>

The following requirements apply across the entire codebase. Their absence directly reduces the grade and will be
checked at every stage demo.

| Requirement | What must be done | Guidance | Done |
|-------------|-------------------|----------|------|
| Javadoc Documentation | All classes and non-trivial methods must have Javadoc comments. Class-level Javadoc must identify the primary author and any secondary authors. Trivial methods such as getters, setters, `toString()`, and constructors with no logic may omit Javadoc. | Use the required Section 11 format. Example: `@author Alex Smith (primary), Bea Jones (contributor)`. Method Javadoc must describe purpose, parameters, return values, and any exceptions thrown. |      |
| `Optional<T>` | Methods that may not find a result must return `Optional<T>` and must never return `null`. This applies to DAO methods such as `getById`, service layer methods, and any method where absence of a value is a valid outcome. | Use `Optional.of(value)` for present values and `Optional.empty()` for absent values. Client code should use `.isPresent()`, `.ifPresent()`, `.orElse()`, or `.orElseThrow()` to reduce `NullPointerException` risk. |      |
| Design Patterns | Apply at least two patterns covered in class and justify both choices in the README. | Suitable examples include Factory for object creation, Singleton for a shared database connection, Strategy for interchangeable encoding or filtering behaviour, Observer for event propagation, and Adapter for integrating incompatible interfaces. |      |
| Generics | Use generic types meaningfully in at least two places and avoid raw types throughout. | Minimum examples include `ServerResponse<T>` for all server replies and a generic DAO interface such as `Dao<T, K>`. `Optional<T>` also supports type-safe nullable returns. |      |
| Functional Interfaces and Lambdas | Use lambda expressions or method references in at least two distinct places. | Examples include `Predicate<T>` for entity filtering, `Function<T, R>` for JSON mapping, or `Comparator` via a lambda in stream operations. Lambdas must replace something meaningful. |      |
| Collections | Select the most appropriate collection type for each data structure. | Use `List<T>` for ordered results and `Map` for lookup by ID or metadata caching. Document these choices in the README. |      |
| DRY Principle | Eliminate duplication across DAO methods, JSON converters, and request handlers. | Extract shared SQL-building, JSON-mapping, or response-wrapping logic into helper methods. |      |
| Architecture Diagram | Provide a one-page annotated diagram showing all tiers and communication paths. The diagram was required at Stage 1 and must be updated if the architecture changes. | The diagram must show `Client -> Server` over sockets, `Server -> DAO -> Database`, the JSON protocol layer, and the binary file upload/retrieval flow. |      |

</details>
