# Supermarket Store System

## Stage 4 Overview

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
| Javadoc Documentation | All classes and non-trivial methods must have Javadoc comments. Class-level Javadoc must identify the primary author and any secondary authors. Trivial methods such as getters, setters, `toString()`, and constructors with no logic may omit Javadoc. Generated HTML output is kept in `docs/javadoc/index.html`. | Use the required Section 11 format. Example: `@author Alex Smith (primary), Bea Jones (contributor)`. Method Javadoc must describe purpose, parameters, return values, and any exceptions thrown. Regenerate the HTML output into `docs/javadoc/`, not the repository root. |      |
| `Optional<T>` | Methods that may not find a result must return `Optional<T>` and must never return `null`. This applies to DAO methods such as `getById`, service layer methods, and any method where absence of a value is a valid outcome. | Use `Optional.of(value)` for present values and `Optional.empty()` for absent values. Client code should use `.isPresent()`, `.ifPresent()`, `.orElse()`, or `.orElseThrow()` to reduce `NullPointerException` risk. |      |
| Design Patterns | Apply at least two patterns covered in class and justify both choices in the README. | Suitable examples include Factory for object creation, Singleton for a shared database connection, Strategy for interchangeable encoding or filtering behaviour, Observer for event propagation, and Adapter for integrating incompatible interfaces. |      |
| Generics | Use generic types meaningfully in at least two places and avoid raw types throughout. | Minimum examples include `ServerResponse<T>` for all server replies and a generic DAO interface such as `Dao<T, K>`. `Optional<T>` also supports type-safe nullable returns. |      |
| Functional Interfaces and Lambdas | Use lambda expressions or method references in at least two distinct places. | Examples include `Predicate<T>` for entity filtering, `Function<T, R>` for JSON mapping, or `Comparator` via a lambda in stream operations. Lambdas must replace something meaningful. |      |
| Collections | Select the most appropriate collection type for each data structure. | Use `List<T>` for ordered results and `Map` for lookup by ID or metadata caching. Document these choices in the README. |      |
| DRY Principle | Eliminate duplication across DAO methods, JSON converters, and request handlers. | Extract shared SQL-building, JSON-mapping, or response-wrapping logic into helper methods. |      |
| Architecture Diagram | Provide a one-page annotated diagram showing all tiers and communication paths. The diagram was required at Stage 1 and must be updated if the architecture changes. | The diagram must show `Client -> Server` over sockets, `Server -> DAO -> Database`, the JSON protocol layer, and the binary file upload/retrieval flow. |      |

</details>

# Assessment and Submission Checklist

## Stage 2 - 50%

| Component | Marks | What We Are Looking For | Hanna | Nikita |
|-----------|-------|--------------------------|-------|--------|
| A. DAO and Persistence Layer (F1-F9) | 16 | Correct and safe JDBC; `PreparedStatement` throughout; `Optional<T>` used for nullable returns; DAO interface and implementation correctly separated; comprehensive Javadoc with author attribution; robust connection management and error handling. Assessed through working code and live demo explanation. |  |  |
| B. JSON Conversion (F9) | 8 | Accurate bidirectional JSON conversion; `xxxToJson()`, `xxxFromJson()`, and `xxxListToJson()` all correct and round-trip verified. |  |  |
| C. Client-Server Communication (F10-F16) | 20 | Functional multithreaded server using `ExecutorService`; `ServerResponse<T>` used consistently; clean documented JSON protocol; client-server separation maintained. Ability to trace a request end-to-end assessed at demo. |  |  |
| D. Process and Repository - Stage 2 | 6 | Meaningful commits spanning both stages; two-client demo successful; README includes protocol documentation; commit messages are descriptive. |  |  |

## Stage 4 - 50%

| Component | Marks | What We Are Looking For | Hanna | Nikita |
|-----------|-------|--------------------------|-------|--------|
| E. Extended Unit Testing - Scope (F23) | 5 | JUnit 5 suite extended to cover all DAO methods, JSON conversion, at least one server request/response scenario, and one binary file upload/retrieval scenario; all tests pass; ability to explain what each test proves assessed at demo. |  |  |
| F. Extended Unit Testing - Coverage (F24) | 4 | `>=70%` line coverage across DAO, JSON conversion, and binary file handling classes demonstrated via IntelliJ coverage panel screenshot committed to `reports/coverage.png`. |  |  |
| G. Binary File Handling (Basic) (F17-F20) | 6 | Schema correctly extended with BLOB column and metadata; binary upload and retrieval working end-to-end; file bytes reconstructed correctly on the client; metadata queryable independently; `PreparedStatement.setBytes()` / `getBinaryStream()` used correctly; design explained at demo. |  |  |
| H. OOP Quality and Design | 6 | Patterns applied purposefully; lambdas and generics used meaningfully; `Optional<T>` used instead of null returns; DRY evident; comprehensive Javadoc on all classes and non-trivial methods with author attribution. All OOP choices justifiable at demo. |  |  |
| I. Screencast | 5 | Part 1 - Demonstrate Functionality (5 marks): all features shown running live end-to-end. Part 2 - Design Choices and Iterations (5 marks): genuine design rationale explained; at least one real decision change shown with code or commit evidence; hypothetical changes receive no marks. |  |  |
| J. Process, Incremental Evidence, and Live Demonstration | 4 | Commit history reflects genuine ongoing development across all stages; all four stage demos passed; contribution matrix credible. |  |  |
| K. Technical Excellence (Optional) | 20 | Optional component for students seeking to demonstrate advanced architectural design. Design and implement a generic service abstraction layer that can handle multiple service types polymorphically, for example file upload/download service, task processing service, or other domain-relevant services. Requirements: (1) service interface or abstract class defining the contract; (2) at least two concrete service implementations; (3) server-side polymorphic service routing and execution; (4) appropriate design pattern application, such as Strategy, Factory, Template Method, or Command; (5) all services fully functional and tested; (6) clear architectural rationale explained at demo. Students who do not attempt this component can still achieve 30/50 in Stage 4. |  |  |

## Submission Checklist

Tip: keep evidence in easy-to-find folders such as `reports/`, `tests/`, or `data/`, and note the paths in the README.

| Item | Stage 1 - Gate (8th Mar) | Stage 2 - Graded (22nd Mar) | Stage 3 - Gate (26th Apr) | Stage 4 - Graded (3rd May) | Hanna | Nikita |
|------|---------------------------|------------------------------|----------------------------|-----------------------------|-------|--------|
| Java source code | Compiles and runs; entities, DAO, CRUD, filter, and JSON complete (F1-F9). | Client-server features working; two simultaneous clients supported (F10-F16). | Binary file upload and retrieval working; metadata query working; core tests passing (F17-F22). | Extended test suite passing; all prior features stable (F23-F24). |  |  |
| `mysqlSetup.sql` | Present; recreates schema and seed data. | Updated if schema changed. | Updated with BLOB column and metadata columns. | Final version. |  |  |
| Architecture diagram | Committed to repo. | Updated if architecture changed. | Updated if architecture changed. | Final version in README. |  |  |
| JUnit tests and coverage | Not required. | Not required. | Core suite passing and committed to repo. | Extended suite passing; IntelliJ coverage screenshot at `reports/coverage.png` with `>=70%` coverage. |  |  |
| Screencast | Not required. | Not required. | Not required. | 8-10 minutes; filename `2025-26-L8-OOP-GCA2-GroupID`. |  |  |
| Contribution matrix | Not required. | Not required. | Not required. | PDF or README table. |  |  |
| README | Domain overview; architecture diagram link. | Protocol documentation; threading rationale. | Testing summary; binary file handling description. | All sections complete; Harvard references. If Component K is attempted, include a dedicated service architecture section, pattern justification, and design rationale. |  |  |
| CA Cover Sheet | Not required. | Not required. | Not required. | Signed electronic copy, one per group. |  |  |

## Appendix A - Assessment Rubric

## A.1 Stage 2 Components (A-D)

| Criterion (Weight) | Excellent (A) | Good (B) | Satisfactory (C) | Limited (D/E) | Unacceptable (F/NS) |
|--------------------|---------------|----------|------------------|---------------|---------------------|
| A. DAO and Persistence (16%) | All CRUD correct, safe, and well-structured; `PreparedStatement` throughout; `Optional<T>` used for nullable returns; robust connection/error handling; comprehensive Javadoc with author attribution; can fully explain all design choices at demo. | All operations correct; safe SQL; `Optional<T>` mostly used; good error handling; Javadoc present; can explain most choices with minor gaps. | Core operations work; `Optional<T>` used in some places; explains the main structure but struggles with detail or edge cases. | Basic CRUD present but fragile; returning `null` in places; limited Javadoc; limited ability to explain design decisions at demo. | Missing or broken CRUD; no Javadoc; cannot explain the code at demo. |
| B. JSON Conversion (8%) | Accurate bidirectional conversion; all three methods correct and explained at demo. | Conversion correct with minor issues; can explain most of it. | Works for main cases; explains the approach broadly. | Partially correct; limited explanation at demo. | Missing or broken; no explanation. |
| C. Client-Server (20%) | Fully functional multithreaded server; `ServerResponse<T>` consistent; clean documented protocol; can trace any request end-to-end at demo. | Functional with minor threading or protocol issues; can trace most requests. | Core features work; explains the structure but struggles with threading detail. | Basic socket communication; fragile threading; inconsistent protocol; explanation weak. | Non-functional; no thread management; protocol absent; cannot explain at demo. |
| D. Process - Stage 2 (6%) | Rich commits spanning both stages; two-client demo flawless; commit messages descriptive and meaningful throughout. | Regular commits; two-client demo works; minor message quality issues. | Some iteration visible; demo worked with minor issues; some vague commit messages. | Sparse history or clustering near deadline; demo issues; messages mostly generic. | Single-dump history; demo failed or group absent. |

## A.2 Stage 4 Components (E-K)

| Criterion (Weight) | Excellent (A) | Good (B) | Satisfactory (C) | Limited (D/E) | Unacceptable (F/NS) |
|--------------------|---------------|----------|------------------|---------------|---------------------|
| E. Unit Testing - Scope (5%) | Comprehensive tests across all DAO methods, JSON conversion, server scenario, and binary file upload/retrieval; all pass; edge cases covered such as file not found and corrupt payload; explains what each test proves at demo. | Good breadth; tests pass; can explain most tests; minor gaps in scope. | Tests present and passing; scope partially covers all layers; explanations adequate. | Limited scope; significant layers untested; cannot explain test purpose. | No meaningful extended tests; Stage 3 baseline not expanded. |
| F. Unit Testing - Coverage (4%) | `>=70%` demonstrated in IntelliJ panel; screenshot committed; coverage reflects full suite. | At or near threshold; screenshot committed; minor class gaps. | Coverage screenshot present; below threshold; explains gap. | Coverage attempted but significantly below threshold; limited evidence. | Coverage not demonstrated; screenshot missing. |
| G. Binary File Handling - Basic (6%) | Schema correctly extended; upload and retrieval fully functional; byte content verified; metadata query works correctly; `setBytes()` / `getBinaryStream()` used correctly; clear explanation at demo. | Functional with minor issues; can explain the approach. | Upload and basic retrieval work; minor metadata issues; explanations adequate. | Partially implemented; retrieval broken or bytes not verified; explanation weak. | Missing or non-functional; cannot explain design. |
| H. OOP Quality (6%) | Patterns applied purposefully and named correctly; lambdas and generics used meaningfully; `Optional<T>` used consistently instead of `null`; DRY throughout; comprehensive Javadoc on all classes and non-trivial methods with proper author attribution; clear explanations at interview. | Patterns correctly applied; `Optional<T>` mostly used; minor documentation gaps; explanations mostly clear. | Patterns present; some lambda/generics; `Optional<T>` used in some places; documentation partially complete; explanations adequate. | Patterns superficially applied; minimal lambdas or generics; still returning `null` in places; incomplete Javadoc; cannot explain choices at demo. | No meaningful pattern, lambda, or generics use; returning `null` throughout; no documentation; no explanation. |
| I. Screencast (5%) | Functionality: all features shown live and complete; binary file upload and retrieval demonstrated with a real file. Design choices: compelling rationale for structural and pattern decisions; a real changed decision shown with code or commit evidence; explanation is specific and credible. | Functionality: all main features shown; minor gaps. Design choices: rationale present; one change discussed; some detail lacking. | Functionality: most features shown; some missing or unclear. Design choices: brief rationale; change described vaguely or without code evidence. | Functionality: significant features missing or not working. Design choices: superficial or generic; no real change discussed. | Screencast missing, inaccessible, or shows no meaningful functionality or design discussion. |
| J. Process, Evidence and Demo (4%) | Rich, consistent commits across all stages; all four demos passed with clear explanations; contribution matrix detailed and credible. | Regular commits; demos mostly passed; matrix present. | Some iteration visible; most demos passed; matrix present. | Sparse history near deadlines; a demo missed or explanation failed; matrix weak. | Single-dump history; demos missed; matrix missing. |
| K. Technical Excellence - Optional (20%) | 18-20 marks: service abstraction expertly designed with clear interface contracts; multiple services fully implemented and working; polymorphic service handling elegant and extensible; advanced patterns such as Strategy + Factory, or Template Method + Command, applied purposefully; clean separation of concerns; architecture demonstrates deep OOP understanding; flawless explanation at demo of design rationale and trade-offs. | Service abstraction well-designed; both services functional; polymorphic handling works; patterns correctly applied; good separation of concerns; can explain most design choices with minor gaps. | Service abstraction present; at least one service working well; polymorphic concept demonstrated but implementation has issues; pattern application adequate; explanations satisfactory but struggles with rationale. | Service abstraction attempted but design flawed; one service partially working; polymorphism superficial; pattern incorrectly applied or forced; cannot fully explain architecture. | Service abstraction missing or non-functional; no meaningful polymorphism; patterns absent; cannot explain approach. |