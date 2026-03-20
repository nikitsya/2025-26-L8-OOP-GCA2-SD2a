package com.supermarketstore.server;

import com.fasterxml.jackson.databind.JsonNode;

import com.supermarketstore.department.Department;
import com.supermarketstore.department.JdbcDepartmentDao;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.ServerResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Routes incoming requests to the appropriate handler by type.
 *
 * @author Hanna Bokariuk
 */
public class RequestRouter {

    // === Fields ===
    private final Map<String, RequestHandler> fHandlers = new HashMap<>();

    // === Constructors ===
    // Creates: a router with all handlers registered against their type constants
    public RequestRouter(JdbcDepartmentDao departmentDao) {
        fHandlers.put("GET_ALL_DEPARTMENTS", req -> handleGetAllDepartments(departmentDao));
        fHandlers.put("GET_DEPARTMENT_BY_ID", req -> handleGetDepartmentById(req, departmentDao));
        fHandlers.put("ADD_DEPARTMENT", req -> handleAddDepartment(req, departmentDao));
//        fHandlers.put("DELETE",     req -> handleDelete(req, departmentDao));
//        fHandlers.put("UPDATE",     req -> handleUpdate(req, departmentDao));
//        fHandlers.put("DISCONNECT", req -> handleDisconnect());
    }

    // === Public API ===
    // Handles: routing a request to its registered handler; returns a failure response for unknown types
    public ServerResponse<?> route(ClientRequest request) {
        RequestHandler handler = fHandlers.get(request.getType());

        if (handler == null)
            return ServerResponse.failure("Unknown request type: " + request.getType());

        try {
            return handler.handle(request);
        }
        catch (Exception e) {
            return ServerResponse.failure("Server error: " + e.getMessage());
        }
    }

    // === Helpers ===
    // Gets: all entities from the DAO and wraps them in a success response
    private ServerResponse<List<Department>> handleGetAllDepartments(JdbcDepartmentDao departmentDao) throws Exception {
        List<Department> departments = departmentDao.getAllDepartments();
        return ServerResponse.success("Departments retrieved successfully", departments);
    }

    // Gets: a single entity by id or returns a failure response if not found
    private ServerResponse<?> handleGetDepartmentById(ClientRequest request, JdbcDepartmentDao departmentDao) throws Exception {
        JsonNode payload = request.getPayload();

        if (payload == null || !payload.has("id"))
            return ServerResponse.failure("Missing required field: id");

        int id = payload.get("id").asInt();

        return departmentDao.getDepartmentById(id)
                .map( department -> ServerResponse.success( "Department retrieved successfully",
                        department))
                .orElseGet(() -> ServerResponse.failure("Department not found for id: "  + id));
    }

    //
}