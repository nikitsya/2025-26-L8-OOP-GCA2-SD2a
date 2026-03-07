# Supermarket Store System

Temporary tracking version of README (to be replaced by final version)

## Project Stage Plan

| Stage | Week  | Deadline          | Focus                                                      | Features | Status                      | Weight |
|-------|-------|-------------------|------------------------------------------------------------|----------|-----------------------------|--------|
| 1     | Wk 6  | Sunday 8th March  | DAO layer, full CRUD, JSON conversion                      | F1-F9    | Mandatory gate - not graded | -      |
| 2     | Wk 8  | Sunday 22nd March | Client-server integration, all CRUD over sockets           | F10-F16  | Graded                      | 50%    |
| 3     | Wk 11 | Sunday 26th April | Binary file handling, protocol completion, core unit tests | F17-F22  | Mandatory gate - not graded | -      |
| 4     | Wk 12 | Sunday 3rd May    | Full test suite with coverage, all features stable         | F23-F24  | Graded                      | 50%    |

### Stage 1 (F1-F9)

| Feature | Hanna | Nikita |
|---------|-------|--------|
| F1      |       | 50%    |
| F2      |       | ✅      |
| F3      |       | ✅      |
| F4      |       | ✅      |
| F5      |       | ✅      |
| F6      |       |        |
| F7      |       |        |
| F8      |       |        |
| F9      |       | ✅      |

#### Required Features

| #  | Feature                               | Specification                                                                                                                                                                               |
|----|---------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| F1 | Entity and Database Setup             | Define a DTO class for each entity with encapsulated, validated fields. Create a `mysqlSetup.sql` file that recreates the schema and populates at least 10 rows of seed data from scratch.  |
| F2 | DAO Interface and JDBC Implementation | Define an `XxxDao` interface. Implement it in `JdbcXxxDao`. The service layer must depend on the interface only. `PreparedStatement` required throughout. No SQL string concatenation.      |
| F3 | Get All Entities                      | `getAllXxx()` returns a `List<T>` of all records.                                                                                                                                           |
| F4 | Get by ID                             | `getXxxById(int id)` returns an `Optional<T>` containing the populated DTO if found, or `Optional.empty()` if not found. Never return `null`.                                               |
| F5 | Delete by ID                          | `deleteXxxById(int id)` removes the record and returns a `boolean` indicating success.                                                                                                      |
| F6 | Insert Entity                         | `insertXxx(T entity)` inserts a new record and returns the populated DTO including the auto-generated ID from `getGeneratedKeys()`.                                                         |
| F7 | Update Entity                         | `updateXxx(int id, T entity)` applies field updates and returns the updated DTO.                                                                                                            |
| F8 | Filter with Predicate                 | `findXxxByFilter(Predicate<T> filter)` returns a `List<T>` of matching entities using a lambda or method reference, not a raw SQL string per filter.                                        |
| F9 | JSON Conversion                       | `xxxToJson(T entity)`, `xxxFromJson(String json)`, and `xxxListToJson(List<T> list)` are all correct and round-trip verified.                                                               |
| -  | Architecture Diagram                  | One-page annotated diagram showing Client -> Server -> DAO -> Database and the JSON protocol layer. Committed to the repo before the Stage 1 deadline. Use Mermaid markdown if appropriate. |

<details>
<summary><strong>Stage 2 (F10-F16)</strong></summary>

| Feature | Hanna | Nikita |
|---------|-------|--------|
| F10     |       |        |
| F11     |       |        |
| F12     |       |        |
| F13     |       |        |
| F14     |       |        |
| F15     |       |        |
| F16     |       |        |

</details>

<details>
<summary><strong>Stage 3 (F17-F22)</strong></summary>

| Feature | Hanna | Nikita |
|---------|-------|--------|
| F17     |       |        |
| F18     |       |        |
| F19     |       |        |
| F20     |       |        |
| F21     |       |        |
| F22     |       |        |

</details>

<details>
<summary><strong>Stage 4 (F23-F24)</strong></summary>

| Feature | Hanna | Nikita |
|---------|-------|--------|
| F23     |       |        |
| F24     |       |        |

</details>
