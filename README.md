# Supermarket Store System

Temporary tracking version of README (to be replaced by final version)

## Project Stage Plan

| Stage | Week  | Deadline          | Focus                                                      | Features | Status                      | Weight |
|-------|-------|-------------------|------------------------------------------------------------|----------|-----------------------------|--------|
| 1     | Wk 6  | Sunday 8th March  | DAO layer, full CRUD, JSON conversion                      | F1-F9    | Mandatory gate - not graded | -      |
| 2     | Wk 8  | Sunday 22nd March | Client-server integration, all CRUD over sockets           | F10-F16  | Graded                      | 50%    |
| 3     | Wk 11 | Sunday 26th April | Binary file handling, protocol completion, core unit tests | F17-F22  | Mandatory gate - not graded | -      |
| 4     | Wk 12 | Sunday 3rd May    | Full test suite with coverage, all features stable         | F23-F24  | Graded                      | 50%    |

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

### Stage 2 (F10-F16)

| #   | Feature                       | Specification                                                                                                                                                                                  | Hanna | Nikita |
|-----|-------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------|--------|
| F10 | Multithreaded Server          | Server uses ExecutorService to handle each connected client on a separate thread. Server does not block on a single client.                                                                    |      |        |
| F11 | ServerResponse\<T\> Wrapper   | All server replies use ServerResponse\<T\> carrying status, message, and data. Raw types not used.                                                                                             |   ✅  |        |
| F12 | Display by ID and Display All | Client sends a JSON request; server calls the DAO and returns the result as ServerResponse\<T\> JSON; client parses and displays. Each team member implements this for their own entity.       |    ✅ |        |
| F13 | Add Entity                    | Client serialises entity data to JSON and sends an insert request. Server returns the new entity (including auto-generated ID) on success, or a structured error response on failure.          |      |        |
| F14 | Delete Entity                 | Client sends a delete request with the target ID. Server calls deleteXxxById() and returns a structured success or failure response.                                                           |      |        |
| F15 | Update Entity                 | Client sends updated field data. Server calls updateXxx() and returns the updated entity.                                                                                                      |      |        |
| F16 | Error Handling and Protocol   | Structured error responses returned for all failure cases - exceptions are not propagated to the client. Protocol documented in README: each request type, payload fields, and response shape. |      |        |

### Checklist to prepare for discussions and implementation work.

1. Explain this method line by line:

```java
@Override
public List<Product> findProductsByFilter(Predicate<Product> filter) {
    if (filter == null) throw new IllegalArgumentException("filter is required");
    return getAllProducts().stream().filter(filter).toList();
}
```

2. Be ready to explain these Java concepts:

- `Stream`: a pipeline API to process collections (map/filter/reduce) without manual loops.
- `filter(...)`: keeps only elements that match a condition.
- `Predicate<T>`: a functional interface with `boolean test(T value)` used for conditions.
- `toList()`: collects stream results into a list.

3. JSON converter reverse methods:

- `DepartmentJsonConverter` currently has `departmentListToJson(List<Department> list)` but no reverse method.
- Add reverse conversion for department lists:
  `List<Department> departmentListFromJson(String json);`

4. Testing guidance:

- Tests should focus on business behavior, not Java library internals.
- Example: testing `String.trim()` itself is low value; keep existing tests for now, but prioritize business-rule tests.

5. Add DAO integration-style tests (required):

- Add a test like `daoInsertProduct()`:
- Create a new product object.
- Insert it with DAO.
- Retrieve it by ID.
- Assert all fields on the retrieved object match inserted values.
- Add similar DAO tests for update and delete paths where possible.

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
