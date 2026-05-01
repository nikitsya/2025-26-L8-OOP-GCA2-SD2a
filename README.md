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
