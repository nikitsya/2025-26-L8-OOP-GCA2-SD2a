package com.supermarketstore.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.supermarketstore.department.Department;
import com.supermarketstore.department.DepartmentDao;
import com.supermarketstore.product.Product;
import com.supermarketstore.product.ProductDao;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.RequestType;
import com.supermarketstore.protocol.ServerResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Routes incoming requests to the appropriate handler by type.
 *
 * @author Hanna Bokariuk (primary)
 * @author Nikita Smiichyk (contributor - product flow, routing updates, and refactoring)
 */
public class RequestRouter {
    // === Fields ===
    private final Map<String, RequestHandler> _handlers = new HashMap<>();

    // === Constructor ===

    /**
     * Creates a router and registers all department and product request handlers
     * under their corresponding request type constants.
     *
     * @param departmentDao data access object used by department-related handlers
     * @param productDao    data access object used by product-related handlers
     */
    public RequestRouter(DepartmentDao departmentDao, ProductDao productDao) {
        _handlers.put(RequestType.GET_ALL_DEPARTMENTS.name(), req -> handleGetAllDepartments(departmentDao));
        _handlers.put(RequestType.GET_DEPARTMENT_BY_ID.name(), req -> handleGetDepartmentById(req, departmentDao));
        _handlers.put(RequestType.ADD_DEPARTMENT.name(), req -> handleAddDepartment(req, departmentDao));
        _handlers.put(RequestType.DELETE_DEPARTMENT_BY_ID.name(), req -> handleDeleteDepartmentById(req, departmentDao));
        _handlers.put(RequestType.UPDATE_DEPARTMENT.name(), req -> handleUpdateDepartment(req, departmentDao));
        _handlers.put(RequestType.GET_ALL_PRODUCTS.name(), req -> handleGetAllProducts(productDao));
        _handlers.put(RequestType.GET_PRODUCT_BY_ID.name(), req -> handleGetProductById(req, productDao));
        _handlers.put(RequestType.ADD_PRODUCT.name(), req -> handleAddProduct(req, productDao));
        _handlers.put(RequestType.DELETE_PRODUCT_BY_ID.name(), req -> handleDeleteProductById(req, productDao));
        _handlers.put(RequestType.UPDATE_PRODUCT.name(), req -> handleUpdateProduct(req, productDao));
    }

    // === Helpers ===

    /**
     * Returns the payload field with the primary name, or falls back to the alternate name
     * when the primary field is missing.
     *
     * @param payload      the request payload that may contain the target field
     * @param primaryName  the preferred field name
     * @param fallbackName the fallback field name used when the primary one is absent
     * @return the matching JSON node, or null if the payload is null or neither field exists
     */
    private static JsonNode getPayloadField(JsonNode payload, String primaryName, String fallbackName) {
        if (payload == null) return null;

        JsonNode node = payload.get(primaryName);
        return node != null ? node : payload.get(fallbackName);
    }

    // === Public API ===

    /**
     * Routes the incoming client request to the matching handler based on its request type.
     *
     * @param request the client request containing the request type and optional payload
     * @return the server response produced by the matching handler, or an error response
     * if the request type is unknown or handler execution fails
     */
    public ServerResponse<?> route(ClientRequest request) {
        RequestHandler handler = _handlers.get(request.getType());

        if (handler == null) return ServerResponse.error("Unknown request type: " + request.getType());

        try {
            return handler.handle(request);
        } catch (Exception e) {
            return ServerResponse.error("Server error: " + e.getMessage());
        }
    }

    // === Department Helpers ===

    private ServerResponse<List<Department>> handleGetAllDepartments(DepartmentDao departmentDao) {
        List<Department> departments = departmentDao.getAllDepartments();
        return ServerResponse.ok("Departments retrieved successfully", departments);
    }

    private ServerResponse<?> handleGetDepartmentById(ClientRequest request, DepartmentDao departmentDao) {
        JsonNode payload = request.getPayload();
        JsonNode idNode = getPayloadField(payload, "id", "id");

        if (idNode == null)
            return ServerResponse.error("Missing required field: id");

        int id = idNode.asInt();

        return departmentDao.getDepartmentById(id)
                .map(department -> ServerResponse.ok("Department retrieved successfully",
                        department))
                .orElseGet(() -> ServerResponse.error("Department not found for id: " + id));
    }

    private ServerResponse<?> handleDeleteDepartmentById(ClientRequest request, DepartmentDao departmentDao) {
        JsonNode payload = request.getPayload();
        JsonNode idNode = getPayloadField(payload, "id", "id");

        if (idNode == null) {
            return ServerResponse.error("Missing required field: id");
        }

        int id = idNode.asInt();
        boolean deleted = departmentDao.deleteDepartmentById(id);

        if (!deleted) {
            return ServerResponse.error("Department not found for id: " + id);
        }

        return ServerResponse.ok("Department deleted successfully", null);
    }

    private ServerResponse<?> handleUpdateDepartment(ClientRequest request, DepartmentDao departmentDao) {
        JsonNode payload = request.getPayload();
        JsonNode idNode = getPayloadField(payload, "id", "id");
        JsonNode nameNode = getPayloadField(payload, "name", "name");
        JsonNode floorNode = getPayloadField(payload, "floor", "floor");
        JsonNode zoneNode = getPayloadField(payload, "zone", "zone");
        JsonNode budgetNode = getPayloadField(payload, "budget", "budget");
        JsonNode employeeCountNode = getPayloadField(payload, "employeeCount", "employeeCount");
        JsonNode refrigeratedNode = getPayloadField(payload, "isRefrigerated", "isRefrigerated");

        if (idNode == null
                || nameNode == null
                || floorNode == null
                || zoneNode == null
                || budgetNode == null
                || employeeCountNode == null
                || refrigeratedNode == null) {
            return ServerResponse.error("Missing required fields: id, name, floor, zone, budget, employeeCount, isRefrigerated");
        }

        int id = idNode.asInt();
        if (departmentDao.getDepartmentById(id).isEmpty()) {
            return ServerResponse.error("Department not found for id: " + id);
        }

        Department updatedDepartment = new Department(
                id,
                nameNode.asText(),
                floorNode.asInt(),
                zoneNode.asInt(),
                budgetNode.asDouble(),
                employeeCountNode.asInt(),
                refrigeratedNode.asBoolean()
        );

        Department result = departmentDao.updateDepartment(id, updatedDepartment);
        return ServerResponse.ok("Department updated successfully", result);
    }

    private ServerResponse<?> handleAddDepartment(ClientRequest request, DepartmentDao departmentDao) {
        JsonNode payload = request.getPayload();
        JsonNode nameNode = getPayloadField(payload, "name", "name");
        JsonNode floorNode = getPayloadField(payload, "floor", "floor");
        JsonNode zoneNode = getPayloadField(payload, "zone", "zone");
        JsonNode budgetNode = getPayloadField(payload, "budget", "budget");
        JsonNode employeeCountNode = getPayloadField(payload, "employeeCount", "employeeCount");
        JsonNode refrigeratedNode = getPayloadField(payload, "isRefrigerated", "isRefrigerated");

        if (nameNode == null
                || floorNode == null
                || zoneNode == null
                || budgetNode == null
                || employeeCountNode == null
                || refrigeratedNode == null) {
            return ServerResponse.error("Missing required fields: name, floor, zone, budget, employeeCount, isRefrigerated");
        }

        Department newDepartment = new Department(
                0,
                nameNode.asText(),
                floorNode.asInt(),
                zoneNode.asInt(),
                budgetNode.asDouble(),
                employeeCountNode.asInt(),
                refrigeratedNode.asBoolean()
        );
        Department insertDepartment = departmentDao.insertDepartment(newDepartment);
        return ServerResponse.ok("Department added successfully", insertDepartment);
    }

    // === Product Helpers ===

    private ServerResponse<List<Product>> handleGetAllProducts(ProductDao productDao) {
        List<Product> products = productDao.getAllProducts();
        return ServerResponse.ok("Products retrieved successfully", products);
    }

    private ServerResponse<?> handleGetProductById(ClientRequest request, ProductDao productDao) {
        JsonNode payload = request.getPayload();
        JsonNode idNode = getPayloadField(payload, "id", "id");
        if (idNode == null) return ServerResponse.error("Missing required field: id");
        int id = idNode.asInt();
        return productDao.getProductById(id)
                .map(product -> ServerResponse.ok("Product retrieved successfully", product))
                .orElseGet(() -> ServerResponse.error("Product not found for id: " + id));
    }

    private ServerResponse<?> handleAddProduct(ClientRequest request, ProductDao productDao) {
        JsonNode payload = request.getPayload();
        JsonNode nameNode = getPayloadField(payload, "name", "name");
        JsonNode priceNode = getPayloadField(payload, "price", "price");
        JsonNode onSaleNode = getPayloadField(payload, "isOnSale", "is_on_sale");
        JsonNode discountNode = getPayloadField(payload, "discountPrice", "discount_price");
        JsonNode stockNode = getPayloadField(payload, "stock", "stock");

        if (nameNode == null || priceNode == null || onSaleNode == null || stockNode == null) {
            return ServerResponse.error("Missing required fields: name, price, isOnSale (or is_on_sale), stock");
        }

        boolean onSale = onSaleNode.asBoolean();
        if (onSale && (discountNode == null || discountNode.isNull())) {
            return ServerResponse.error("Missing required field: discountPrice (or discount_price) when product is on sale");
        }

        Double discountPrice = (discountNode == null || discountNode.isNull()) ? null : discountNode.asDouble();
        Product newProduct = new Product(
                0,
                nameNode.asText(),
                priceNode.asDouble(),
                onSale,
                discountPrice,
                stockNode.asInt()
        );

        Product insertedProduct = productDao.insertProduct(newProduct);
        return ServerResponse.ok("Product added successfully", insertedProduct);
    }

    private ServerResponse<?> handleDeleteProductById(ClientRequest request, ProductDao productDao) {
        JsonNode payload = request.getPayload();
        JsonNode idNode = getPayloadField(payload, "id", "id");
        if (idNode == null) return ServerResponse.error("Missing required field: id");
        int id = idNode.asInt();
        boolean deleted = productDao.deleteProductById(id);
        if (!deleted) return ServerResponse.error("Product not found for id: " + id);
        return ServerResponse.ok("Product deleted successfully", null);
    }

    private ServerResponse<?> handleUpdateProduct(ClientRequest request, ProductDao productDao) {
        JsonNode payload = request.getPayload();
        JsonNode idNode = getPayloadField(payload, "id", "id");
        JsonNode nameNode = getPayloadField(payload, "name", "name");
        JsonNode priceNode = getPayloadField(payload, "price", "price");
        JsonNode onSaleNode = getPayloadField(payload, "isOnSale", "is_on_sale");
        JsonNode discountNode = getPayloadField(payload, "discountPrice", "discount_price");
        JsonNode stockNode = getPayloadField(payload, "stock", "stock");

        if (idNode == null || nameNode == null || priceNode == null || onSaleNode == null || stockNode == null) {
            return ServerResponse.error("Missing required fields: id, name, price, isOnSale (or is_on_sale), stock");
        }

        int id = idNode.asInt();
        if (productDao.getProductById(id).isEmpty()) {
            return ServerResponse.error("Product not found for id: " + id);
        }

        boolean onSale = onSaleNode.asBoolean();
        if (onSale && (discountNode == null || discountNode.isNull())) {
            return ServerResponse.error("Missing required field: discountPrice (or discount_price) when product is on sale");
        }

        Double discountPrice = (discountNode == null || discountNode.isNull()) ? null : discountNode.asDouble();

        Product updatedProduct = new Product(
                id,
                nameNode.asText(),
                priceNode.asDouble(),
                onSale,
                discountPrice,
                stockNode.asInt()
        );

        Product savedProduct = productDao.updateProduct(id, updatedProduct);
        return ServerResponse.ok("Product updated successfully", savedProduct);
    }
}
