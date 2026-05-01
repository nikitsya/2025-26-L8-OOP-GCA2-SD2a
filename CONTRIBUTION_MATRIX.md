# Project Contribution Matrix

This matrix follows the Stage 4 submission format. The primary author is the person who wrote the initial
implementation for that task. The contributor/reviewer column records meaningful review, debugging, refactoring, or
extension work. All group members are expected to understand every part of the code for the final interview.

| Feature / Task | Primary author | Reviewer / contributor | Estimated effort (hours) | Notes |
|:--|:--|:--|--:|:--|
| Domain proposal and entity list | Nikita Smiichyk | Hanna Bokariuk | 2 | Supermarket domain selected with Departments and Products as the main entities. |
| Repository setup, branch structure, and Maven project setup | Nikita Smiichyk | Hanna Bokariuk | 4 | Maven Java 17 project with Jackson, MySQL Connector/J, and JUnit 5 dependencies. |
| `mysqlSetup.sql` department schema and department seed data | Hanna Bokariuk | Nikita Smiichyk | 4 | Created department table, department fields, validation-aligned columns, and seed records. |
| `mysqlSetup.sql` product schema, product seed data, and bridge table | Nikita Smiichyk | Hanna Bokariuk | 5 | Added product table, product seed data, and `department_products` relationship table. |
| Department DTO/entity modelling and validation | Hanna Bokariuk | Nikita Smiichyk | 6 | Implemented department fields, constructors, getters/setters, validation, equality, and Javadocs. |
| Product DTO/entity modelling and validation | Nikita Smiichyk | Hanna Bokariuk | 8 | Implemented product fields, sale/discount validation, JSON properties, equality, defensive byte array copying, and Javadocs. |
| Department DAO interface | Hanna Bokariuk | Nikita Smiichyk | 2 | Created `DepartmentDao` contract with CRUD, filtering, and image retrieval methods. |
| Product DAO interface | Nikita Smiichyk | Hanna Bokariuk | 2 | Created `ProductDao` contract with CRUD, filtering, and image retrieval methods. |
| Department JDBC DAO: `getAll` and `getById` | Hanna Bokariuk | Nikita Smiichyk | 4 | Implemented metadata-only department reads using `Optional<Department>` and `PreparedStatement`. |
| Product JDBC DAO: `getAll` and `getById` | Nikita Smiichyk | Hanna Bokariuk | 4 | Implemented metadata-only product reads using `Optional<Product>` and `PreparedStatement`. |
| Department JDBC DAO: insert with generated keys | Hanna Bokariuk | Nikita Smiichyk | 3 | Implemented insert and returned the generated `department_id`. |
| Product JDBC DAO: insert with generated keys | Nikita Smiichyk | Hanna Bokariuk | 4 | Implemented insert, shared parameter binding, generated key handling, and validation. |
| Department JDBC DAO: update and delete | Hanna Bokariuk | Nikita Smiichyk | 4 | Implemented department update/delete with consistent return and error behaviour. |
| Product JDBC DAO: update and delete | Nikita Smiichyk | Hanna Bokariuk | 4 | Implemented product update/delete with row-count checks and clearer SQL error handling. |
| Department predicate filtering | Hanna Bokariuk | Nikita Smiichyk | 2 | Implemented `findDepartmentsByFilter(Predicate<Department>)`. |
| Product predicate filtering | Nikita Smiichyk | Hanna Bokariuk | 2 | Implemented `findProductsByFilter(Predicate<Product>)`. |
| Department JSON conversion | Hanna Bokariuk | Nikita Smiichyk | 4 | Implemented department to/from JSON and list conversion with Jackson. |
| Product JSON conversion | Nikita Smiichyk | Hanna Bokariuk | 5 | Implemented product to/from JSON, list conversion, and error handling with Jackson. |
| Architecture diagram and tier explanation | Nikita Smiichyk | Hanna Bokariuk | 2 | Created Mermaid architecture documentation showing client, protocol, server, DAO, and database layers. |
| Multithreaded server with `ExecutorService` | Nikita Smiichyk | Hanna Bokariuk | 7 | Built socket server setup, router wiring, client handling, and per-client thread pool execution. |
| `ServerResponse<T>` wrapper and response mapping | Nikita Smiichyk | Hanna Bokariuk | 4 | Standardised server replies with `status`, `message`, and `data`; used typed responses throughout. |
| Shared request protocol classes | Nikita Smiichyk | Hanna Bokariuk | 4 | Maintained `ClientRequest`, `RequestType`, and shared request/response structure. |
| Protocol documentation in README | Nikita Smiichyk | Hanna Bokariuk | 3 | Documented request types, payloads, response shapes, binary handling, and error responses. Hanna mainly added small status/protocol notes. |
| Department client feature: display all and display by ID | Hanna Bokariuk | Nikita Smiichyk | 3 | Implemented and demonstrated department read requests over sockets. |
| Product client feature: display all and display by ID | Nikita Smiichyk | Hanna Bokariuk | 3 | Implemented and demonstrated product read requests over sockets. |
| Department client feature: insert/update/delete over sockets | Hanna Bokariuk | Nikita Smiichyk | 5 | Implemented department add, update, delete handlers and client demo verification. |
| Product client feature: insert/update/delete over sockets | Nikita Smiichyk | Hanna Bokariuk | 5 | Implemented product add, update, delete handlers and client demo verification. |
| Shared client request helper/refactoring | Nikita Smiichyk | Hanna Bokariuk | 5 | Extracted reusable request sending, entity fetching, list display, delete, and response printing helpers. |
| Error handling and server logging | Nikita Smiichyk | Hanna Bokariuk | 4 | Added structured error responses and server-side logging for invalid JSON, unknown requests, connection events, and handler failures. |
| Department binary schema extension | Hanna Bokariuk | Nikita Smiichyk | 3 | Added department image BLOB and metadata columns, later using `MEDIUMBLOB`. |
| Product binary schema extension | Nikita Smiichyk | Hanna Bokariuk | 3 | Added product image BLOB and metadata columns, later using `MEDIUMBLOB`. |
| Department binary DTO and DAO support | Hanna Bokariuk | Nikita Smiichyk | 5 | Added department image bytes, file metadata, DAO storage, and image retrieval. |
| Product binary DTO and DAO support | Nikita Smiichyk | Hanna Bokariuk | 6 | Added product image bytes, metadata validation, DAO storage, metadata-only mapping, and image retrieval. |
| Shared binary upload payload builder | Nikita Smiichyk | Hanna Bokariuk | 4 | Created `FilePayloadBuilder` to read files, Base64-encode bytes, and add file metadata to JSON payloads. |
| Department binary upload over sockets | Hanna Bokariuk | Nikita Smiichyk | 4 | Added department file payload handling to add/update requests and stored bytes/metadata in the database. |
| Product binary upload over sockets | Nikita Smiichyk | Hanna Bokariuk | 4 | Added product file payload handling, Base64 decoding, validation, and database storage. |
| Department binary retrieval and client file reconstruction | Hanna Bokariuk | Nikita Smiichyk | 5 | Implemented department image retrieval request, DAO BLOB fetch, client-side metadata printing, and file saving. |
| Product binary retrieval and client file reconstruction | Nikita Smiichyk | Hanna Bokariuk | 5 | Implemented product image retrieval request, DAO BLOB fetch, metadata-only separation, and file retrieval flow. |
| Metadata-only query without BLOB fetch | Nikita Smiichyk | Hanna Bokariuk | 3 | Implemented through `GET_DEPARTMENT_BY_ID` and `GET_PRODUCT_BY_ID`, where DAO SQL selects metadata but not BLOB columns. |
| Disconnect protocol and cleanup | Hanna Bokariuk | Nikita Smiichyk | 3 | Added `DISCONNECT`, route handling, client request before shutdown, and clean server client-loop exit. |
| Department Stage 3 core tests | Hanna Bokariuk | Nikita Smiichyk | 5 | Added department DAO read/insert/update/delete/filter tests and JSON round-trip tests. |
| Product Stage 3 core tests | Nikita Smiichyk | Hanna Bokariuk | 6 | Added product DAO read/insert/update/delete/filter tests and JSON round-trip tests. |
| Department binary tests | Hanna Bokariuk | Nikita Smiichyk | 3 | Added department DAO file metadata and image byte assertions. |
| Product binary tests | Nikita Smiichyk | Hanna Bokariuk | 4 | Added product metadata-only read tests, image retrieval tests, and binary byte assertions. |
| Product extended validation and failure tests | Nikita Smiichyk | Hanna Bokariuk | 8 | Added product constructor, validation, JSON failure, DAO connection failure, and database failure tests. |
| Test clean-up and fixture management | Nikita Smiichyk | Hanna Bokariuk | 4 | Added test row cleanup for product DAO tests and removed low-value product tests. |
| Generated Javadocs and documentation organisation | Nikita Smiichyk | Hanna Bokariuk | 3 | Generated and organised Javadocs under `docs/javadoc`; Hanna added department class documentation. |
| Final GCA2 README | Nikita Smiichyk | Hanna Bokariuk | 2 | Wrote final project overview, run instructions, protocol docs, stage evidence, binary notes, OOP features, references, and submission checklist. |
| Contribution matrix | Nikita Smiichyk | Hanna Bokariuk | 3 | Reworked the matrix into the final Stage 4 table format based on Git history and task ownership. |
| Assessment checklist and final submission notes | Nikita Smiichyk | Hanna Bokariuk | 1 | Added final checklist, assessment rubric notes, stage tracking, and final submission reminders. |
| Coverage evidence screenshot `/reports/coverage.png` | Nikita Smiichyk | Hanna Bokariuk | 1 | Must be generated in IntelliJ IDEA using the full test suite before final submission. |
| Screencast planning and export | Hanna Bokariuk | Nikita Smiichyk | 3 | Should cover both vertical slices, server/client demo, binary handling, tests, and design explanation. |
| Harvard references and AI usage declaration | Nikita Smiichyk | Hanna Bokariuk | 2 | Added references and AI tool use declaration in the final README. |
| Final code formatting and clean-up | Nikita Smiichyk | Hanna Bokariuk | 3 | Performed project-wide formatting, package clean-up, unused asset removal, and shared refactoring. |
