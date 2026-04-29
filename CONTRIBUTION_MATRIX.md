# Project Contribution Matrix

This matrix records how the project work was divided across the supermarket store system. It is organised around the
two main domain areas first, because the Department and Product features were implemented as parallel vertical slices.

## Domain Ownership

- Hanna Bokariuk - Department features
- Nikita Smiichyk - Product features

## Team Members

| Name | Student ID |
|------|------------|
| Hanna Bokariuk | D00283065 |
| Nikita Smiichyk | D00283070 |

## Contribution Matrix

| Feature / Task | Primary author | Reviewer / contributor | Estimated effort (hours) | Notes |
|----------------|----------------|------------------------|--------------------------|-------|
| F1 - Department entity and database setup | Hanna Bokariuk |  |  | Department DTO, validation rules, schema fields, image metadata, and seed data. |
| F1 - Product entity and database setup | Nikita Smiichyk |  |  | Product DTO, validation rules, schema fields, image metadata, and seed data. |
| F2 - Department DAO interface and JDBC implementation | Hanna Bokariuk |  |  | Department DAO contract and JDBC implementation using `PreparedStatement`. |
| F2 - Product DAO interface and JDBC implementation | Nikita Smiichyk |  |  | Product DAO contract and JDBC implementation using `PreparedStatement`. |
| F3 - Department get all entities | Hanna Bokariuk |  |  | Department read-all DAO method. |
| F3 - Product get all entities | Nikita Smiichyk |  |  | Product read-all DAO method. |
| F4 - Department get by ID | Hanna Bokariuk |  |  | Department lookup method returning `Optional<Department>`. |
| F4 - Product get by ID | Nikita Smiichyk |  |  | Product lookup method returning `Optional<Product>`. |
| F5 - Department delete by ID | Hanna Bokariuk |  |  | Department delete method and success/failure return behaviour. |
| F5 - Product delete by ID | Nikita Smiichyk |  |  | Product delete method and success/failure return behaviour. |
| F6 - Department insert entity | Hanna Bokariuk |  |  | Department insert method returning generated primary key. |
| F6 - Product insert entity | Nikita Smiichyk |  |  | Product insert method returning generated primary key. |
| F7 - Department update entity | Hanna Bokariuk |  |  | Department update method and returned updated DTO. |
| F7 - Product update entity | Nikita Smiichyk |  |  | Product update method and returned updated DTO. |
| F8 - Department filter with predicate | Hanna Bokariuk |  |  | Department filtering using `Predicate<Department>`. |
| F8 - Product filter with predicate | Nikita Smiichyk |  |  | Product filtering using `Predicate<Product>`. |
| F9 - Department JSON conversion | Hanna Bokariuk |  |  | Department JSON serialisation, deserialisation, and round-trip behaviour. |
| F9 - Product JSON conversion | Nikita Smiichyk |  |  | Product JSON serialisation, deserialisation, and round-trip behaviour. |
| F10 - Multithreaded server | Nikita Smiichyk | Hanna Bokariuk |  | Nikita implemented the `ExecutorService` client pool and multithreaded accept loop; Hanna created the initial socket server skeleton and later updated the client loop for `DISCONNECT`. |
| F11 - `ServerResponse<T>` wrapper | Hanna Bokariuk | Nikita Smiichyk |  | Hanna added the initial generic `ServerResponse<T>` wrapper; Nikita later refined it with the starter-compatible structure, `OK`/`ERROR` helper methods, and updated routing code to use it consistently. |
| F12 - Department display by ID and display all | Hanna Bokariuk |  |  | Department client-server read flows. |
| F12 - Product display by ID and display all | Nikita Smiichyk |  |  | Product client-server read flows. |
| F13 - Department add entity | Hanna Bokariuk |  |  | Department client-server insert flow and structured response handling. |
| F13 - Product add entity | Nikita Smiichyk |  |  | Product client-server insert flow and structured response handling. |
| F14 - Department delete entity | Hanna Bokariuk |  |  | Department client-server delete flow and not-found handling. |
| F14 - Product delete entity | Nikita Smiichyk |  |  | Product client-server delete flow and not-found handling. |
| F15 - Department update entity | Hanna Bokariuk |  |  | Department client-server update flow and validation handling. |
| F15 - Product update entity | Nikita Smiichyk |  |  | Product client-server update flow and validation handling. |
| F16 - Department error handling and protocol | Hanna Bokariuk |  |  | Department request payloads, structured failures, and response mapping. |
| F16 - Product error handling and protocol | Nikita Smiichyk |  |  | Product request payloads, structured failures, and response mapping. |
| F16 - Shared protocol structure |  |  |  | Shared request and response shape, unknown request handling, and protocol consistency. |
| F17 - Department binary schema extension | Hanna Bokariuk |  |  | Department BLOB column, metadata fields, and DTO image support. |
| F17 - Product binary schema extension | Nikita Smiichyk |  |  | Product BLOB column, metadata fields, and DTO image support. |
| F18 - Department binary file upload | Hanna Bokariuk |  |  | Department Base64 upload request, server decode, and database storage. |
| F18 - Product binary file upload | Nikita Smiichyk |  |  | Product Base64 upload request, server decode, and database storage. |
| F19 - Department binary file retrieval | Hanna Bokariuk |  |  | Department image retrieval request, Base64 response, and file reconstruction. |
| F19 - Product binary file retrieval | Nikita Smiichyk |  |  | Product image retrieval request, Base64 response, and file reconstruction. |
| F20 - Department file metadata query | Hanna Bokariuk |  |  | Department metadata-only retrieval without downloading full binary content. |
| F20 - Product file metadata query | Nikita Smiichyk |  |  | Product metadata-only retrieval without downloading full binary content. |
| F21 - Disconnect / exit | Hanna Bokariuk |  |  | Shared disconnect request and clean server thread release. |
| F22 - Department core unit tests | Hanna Bokariuk |  |  | Stage 3 baseline tests for Department DAO read, insert, and JSON round trip. |
| F22 - Product core unit tests | Nikita Smiichyk |  |  | Stage 3 baseline tests for Product DAO read, insert, and JSON round trip. |
| F23 - Department extended test suite | Hanna Bokariuk |  |  | Stage 4 Department tests for DAO, JSON conversion, server handling, and binary flow. |
| F23 - Product extended test suite | Nikita Smiichyk |  |  | Stage 4 Product tests for DAO, JSON conversion, server handling, and binary flow. |
| F23 - Shared server request/response tests |  |  |  | Shared server-side request handling tests. |
| F24 - Coverage threshold |  |  |  | IntelliJ IDEA coverage run and `reports/coverage.png` evidence. |
| Architecture diagram | Nikita Smiichyk |  |  | One-page annotated architecture diagram and updates after architecture changes. |
| JUnit test maintenance |  |  |  | Test fixtures, assertions, debugging, and suite maintenance. |
| README | Nikita Smiichyk | Hanna Bokariuk |  | Setup guide, protocol documentation, architecture summary, testing evidence, and references. |
| Screencast |  |  |  | 8-10 minute final demo recording and exported submission file. |

## Optional Component K - Technical Excellence

| Feature / Task | Primary author | Reviewer / contributor | Estimated effort (hours) | Notes |
|----------------|----------------|------------------------|--------------------------|-------|
| Component K - Service abstraction design |  |  |  | Generic service interface or abstract class with execution, validation, and result handling contract. |
| Component K - `FileUploadService` implementation |  |  |  | Concrete service for binary file upload or retrieval behaviour. |
| Component K - Second concrete service implementation |  |  |  | Replace with the chosen service, such as `TaskProcessingService` or `ReportGenerationService`. |
| Component K - Service routing logic |  |  |  | Polymorphic service routing and service instantiation pattern. |
| Component K - Service tests and documentation |  |  |  | JUnit tests, README design rationale, and architecture diagram update. |
