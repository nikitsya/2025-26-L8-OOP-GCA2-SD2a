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

    // === Constructors ===
    // Creates: a router with all handlers registered against their type constants
    public RequestRouter(DepartmentDao departmentDao, ProductDao productDao) {
        _handlers.put(RequestType.GET_ALL_DEPARTMENTS.name(), req -> handleGetAllDepartments(departmentDao));
        _handlers.put(RequestType.GET_DEPARTMENT_BY_ID.name(), req -> handleGetDepartmentById(req, departmentDao));
        _handlers.put(RequestType.ADD_DEPARTMENT.name(), req -> handleAddDepartment(req, departmentDao));
        _handlers.put(RequestType.GET_ALL_PRODUCTS.name(), req -> handleGetAllProducts(productDao));
        _handlers.put(RequestType.GET_PRODUCT_BY_ID.name(), req -> handleGetProductById(req, productDao));
        _handlers.put(RequestType.ADD_PRODUCT.name(), req -> handleAddProduct(req, productDao));
    }

    // === Public API ===
    public ServerResponse<?> route(ClientRequest request) {
        RequestHandler handler = _handlers.get(request.getType());

        if (handler == null)
            return ServerResponse.error("Unknown request type: " + request.getType());

        try {
            return handler.handle(request);
        } catch (Exception e) {
            return ServerResponse.error("Server error: " + e.getMessage());
        }
    }

    // === Helpers ===
    private ServerResponse<List<Department>> handleGetAllDepartments(DepartmentDao departmentDao) {
        List<Department> departments = departmentDao.getAllDepartments();
        return ServerResponse.ok("Departments retrieved successfully", departments);
    }

    private ServerResponse<?> handleGetDepartmentById(ClientRequest request, DepartmentDao departmentDao) {
        JsonNode payload = request.getPayload();

        if (payload == null || !payload.has("id"))
            return ServerResponse.error("Missing required field: id");

        int id = payload.get("id").asInt();

        return departmentDao.getDepartmentById(id)
                .map(department -> ServerResponse.ok("Department retrieved successfully",
                        department))
                .orElseGet(() -> ServerResponse.error("Department not found for id: " + id));
    }

    private ServerResponse<?> handleAddDepartment(ClientRequest request, DepartmentDao departmentDao) {
        JsonNode payload = request.getPayload();

        if (payload == null || !payload.has("name") || !payload.has("floor") || !payload.has("zone") || !payload.has("budget") || !payload.has("employeeCount") || !payload.has("isRefrigerated")) {

            return ServerResponse.error("Missing required fields: name, floor, zone, budget, employeeCount, isRefrigerated");
        }
        Department newDepartment = new Department(0, payload.get("name").asText(), payload.get("floor").asInt(), payload.get("zone").asInt(), payload.get("budget").asDouble(), payload.get("employeeCount").asInt(), payload.get("isRefrigerated").asBoolean()
        );
        Department insertDepartment = departmentDao.insertDepartment(newDepartment);
        return ServerResponse.ok("Department added successfully", insertDepartment);
    }

    private ServerResponse<List<Product>> handleGetAllProducts(ProductDao productDao) {
        List<Product> products = productDao.getAllProducts();
        return ServerResponse.ok("Products retrieved successfully", products);
    }

    private ServerResponse<?> handleGetProductById(ClientRequest request, ProductDao productDao) {
        JsonNode payload = request.getPayload();

        if (payload == null || !payload.has("id"))
            return ServerResponse.error("Missing required field: id");

        int id = payload.get("id").asInt();

        return productDao.getProductById(id)
                .map(product -> ServerResponse.ok("Product retrieved successfully", product))
                .orElseGet(() -> ServerResponse.error("Product not found for id: " + id));
    }

    private ServerResponse<?> handleAddProduct(ClientRequest request, ProductDao productDao) {
        JsonNode payload = request.getPayload();
        JsonNode onSaleNode = payload == null ? null : payload.get("isOnSale");
        if (onSaleNode == null && payload != null) onSaleNode = payload.get("is_on_sale");

        JsonNode discountNode = payload == null ? null : payload.get("discountPrice");
        if (discountNode == null && payload != null) discountNode = payload.get("discount_price");

        if (payload == null || !payload.has("name") || !payload.has("price") || onSaleNode == null || !payload.has("stock")) {
            return ServerResponse.error("Missing required fields: name, price, isOnSale (or is_on_sale), stock");
        }

        boolean onSale = onSaleNode.asBoolean();
        if (onSale && (discountNode == null || discountNode.isNull())) {
            return ServerResponse.error("Missing required field: discountPrice (or discount_price) when product is on sale");
        }

        Double discountPrice = (discountNode == null || discountNode.isNull()) ? null : discountNode.asDouble();
        Product newProduct = new Product(
                0,
                payload.get("name").asText(),
                payload.get("price").asDouble(),
                onSale,
                discountPrice,
                payload.get("stock").asInt()
        );

        Product insertedProduct = productDao.insertProduct(newProduct);
        return ServerResponse.ok("Product added successfully", insertedProduct);
    }
}
