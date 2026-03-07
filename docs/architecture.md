# Stage 1 Architecture Diagram

This diagram shows the required Stage 1 flow:
Client -> JSON Protocol Layer -> Server -> DAO -> Database.

```mermaid
flowchart LR
    C["Client (Console/GUI)"]
    J["JSON Protocol Layer\n(Request/Response DTOs)"]
    S["Server Layer\n(Socket Listener + Request Handlers)"]
    D["DAO Layer\n(XxxDao Interfaces + JdbcXxxDao Implementations)"]
    DB[("MySQL Database\n(supermarket_store_system)")]

    C <--> J
    J <--> S
    S --> D
    D --> DB
```

## Annotations

- `Client`: sends requests and receives responses.
- `JSON Protocol Layer`: serializes and deserializes payloads to JSON.
- `Server Layer`: receives requests and delegates operations.
- `DAO Layer`: isolates persistence behind DAO interfaces and JDBC implementations.
- `Database`: stores persistent data in MySQL tables.
