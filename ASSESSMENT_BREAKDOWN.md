# Assessment Breakdown

## Stage 2 - 50% of GCA2 Grade

| Component | Marks | What We Are Looking For | Hanna | Nikita |
|-----------|-------|--------------------------|-------|--------|
| A. DAO and Persistence Layer (F1-F9) | 16 | Correct and safe JDBC; `PreparedStatement` throughout; `Optional<T>` used for nullable returns; DAO interface and implementation correctly separated; comprehensive Javadoc with author attribution; robust connection management and error handling. Assessed through working code and live demo explanation. |  |  |
| B. JSON Conversion (F9) | 8 | Accurate bidirectional JSON conversion; `xxxToJson()`, `xxxFromJson()`, and `xxxListToJson()` all correct and round-trip verified. |  |  |
| C. Client-Server Communication (F10-F16) | 20 | Functional multithreaded server using `ExecutorService`; `ServerResponse<T>` used consistently; clean documented JSON protocol; client-server separation maintained. Ability to trace a request end-to-end assessed at demo. |  |  |
| D. Process and Repository - Stage 2 | 6 | Meaningful commits spanning both stages; two-client demo successful; README includes protocol documentation; commit messages are descriptive. |  |  |

## Stage 4 - 50% of GCA2 Grade

| Component | Marks | What We Are Looking For | Hanna | Nikita |
|-----------|-------|--------------------------|-------|--------|
| E. Extended Unit Testing - Scope (F23) | 5 | JUnit 5 suite extended to cover all DAO methods, JSON conversion, at least one server request/response scenario, and one binary file upload/retrieval scenario; all tests pass; ability to explain what each test proves assessed at demo. |  |  |
| F. Extended Unit Testing - Coverage (F24) | 4 | `>=70%` line coverage across DAO, JSON conversion, and binary file handling classes demonstrated via IntelliJ coverage panel screenshot committed to `reports/coverage.png`. |  |  |
| G. Binary File Handling (Basic) (F17-F20) | 6 | Schema correctly extended with BLOB column and metadata; binary upload and retrieval working end-to-end; file bytes reconstructed correctly on the client; metadata queryable independently; `PreparedStatement.setBytes()` / `getBinaryStream()` used correctly; design explained at demo. |  |  |
| H. OOP Quality and Design | 6 | Patterns applied purposefully; lambdas and generics used meaningfully; `Optional<T>` used instead of null returns; DRY evident; comprehensive Javadoc on all classes and non-trivial methods with author attribution. All OOP choices justifiable at demo. |  |  |
| I. Screencast | 5 | Part 1 - Demonstrate Functionality (5 marks): all features shown running live end-to-end. Part 2 - Design Choices and Iterations (5 marks): genuine design rationale explained; at least one real decision change shown with code or commit evidence; hypothetical changes receive no marks. |  |  |
| J. Process, Incremental Evidence, and Live Demonstration | 4 | Commit history reflects genuine ongoing development across all stages; all four stage demos passed; contribution matrix credible. |  |  |
| K. Technical Excellence (Optional) | 20 | Optional component for students seeking to demonstrate advanced architectural design. Design and implement a generic service abstraction layer that can handle multiple service types polymorphically, for example file upload/download service, task processing service, or other domain-relevant services. Requirements: (1) service interface or abstract class defining the contract; (2) at least two concrete service implementations; (3) server-side polymorphic service routing and execution; (4) appropriate design pattern application, such as Strategy, Factory, Template Method, or Command; (5) all services fully functional and tested; (6) clear architectural rationale explained at demo. Students who do not attempt this component can still achieve 30/50 in Stage 4. |  |  |
| Base Total (E-J) | 30 |  |  |  |
| Maximum Available (E-K) | 50 |  |  |  |
