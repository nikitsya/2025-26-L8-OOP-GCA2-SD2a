package com.supermarketstore.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.supermarketstore.department.Department;
import com.supermarketstore.department.DepartmentDao;
import com.supermarketstore.product.Product;
import com.supermarketstore.product.ProductDao;
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
    public RequestRouter(DepartmentDao departmentDao, ProductDao productDao) {
        fHandlers.put("GET_ALL_DEPARTMENTS", req -> handleGetAllDepartments(departmentDao));
        fHandlers.put("GET_DEPARTMENT_BY_ID", req -> handleGetDepartmentById(req, departmentDao));
        fHandlers.put("ADD_DEPARTMENT", req -> handleAddDepartment(req, departmentDao));
        fHandlers.put("GET_ALL_PRODUCTS", req -> handleGetAllProducts(productDao));
        fHandlers.put("GET_PRODUCT_BY_ID", req -> handleGetProductById(req, productDao));
        fHandlers.put("ADD_PRODUCT", req -> handleAddProduct(req, productDao));
    }

    // === Public API ===
    public ServerResponse<?> route(ClientRequest request) {
        RequestHandler handler = fHandlers.get(request.getType());

        if (handler == null)
            return ServerResponse.failure("Unknown request type: " + request.getType());

        try {
            return handler.handle(request);
        } catch (Exception e) {
            return ServerResponse.failure("Server error: " + e.getMessage());
        }
    }

    // === Helpers ===
    private ServerResponse<List<Department>> handleGetAllDepartments(DepartmentDao departmentDao) throws Exception {
        List<Department> departments = departmentDao.getAllDepartments();
        return ServerResponse.success("Departments retrieved successfully", departments);
    }

    private ServerResponse<?> handleGetDepartmentById(ClientRequest request, DepartmentDao departmentDao) throws Exception {
        JsonNode payload = request.getPayload();

        if (payload == null || !payload.has("id"))
            return ServerResponse.failure("Missing required field: id");

        int id = payload.get("id").asInt();

        return departmentDao.getDepartmentById(id)
                .map(department -> ServerResponse.success("Department retrieved successfully",
                        department))
                .orElseGet(() -> ServerResponse.failure("Department not found for id: " + id));
    }

    private ServerResponse<?> handleAddDepartment(ClientRequest request, DepartmentDao departmentDao) throws Exception {
        JsonNode payload = request.getPayload();

        if (payload == null || !payload.has("name") || !payload.has("floor") || !payload.has("zone") || !payload.has("budget") || !payload.has("employeeCount") || !payload.has("isRefrigerated")) {

            return ServerResponse.failure("Missing required fields: name, floor, zone, budget, employeeCount, isRefrigerated");
        }
        Department newDepartment = new Department(0, payload.get("name").asText(), payload.get("floor").asInt(), payload.get("zone").asInt(), payload.get("budget").asDouble(), payload.get("employeeCount").asInt(), payload.get("isRefrigerated").asBoolean()
        );
        Department insertDepartment = departmentDao.insertDepartment(newDepartment);
        return ServerResponse.success("Department added successfully", insertDepartment);
    }

    private ServerResponse<List<Product>> handleGetAllProducts(ProductDao productDao) throws Exception {
        List<Product> products = productDao.getAllProducts();
        return ServerResponse.success("Products retrieved successfully", products);
    }

    private ServerResponse<?> handleGetProductById(ClientRequest request, ProductDao productDao) throws Exception {
        return null;
    }

    private ServerResponse<?> handleAddProduct(ClientRequest request, ProductDao productDao) throws Exception {
        JsonNode payload = request.getPayload();
        JsonNode onSaleNode = payload == null ? null : payload.get("isOnSale");
        if (onSaleNode == null && payload != null) onSaleNode = payload.get("is_on_sale");

        JsonNode discountNode = payload == null ? null : payload.get("discountPrice");
        if (discountNode == null && payload != null) discountNode = payload.get("discount_price");

        if (payload == null || !payload.has("name") || !payload.has("price") || onSaleNode == null || !payload.has("stock")) {
            return ServerResponse.failure("Missing required fields: name, price, isOnSale (or is_on_sale), stock");
        }

        boolean onSale = onSaleNode.asBoolean();
        if (onSale && (discountNode == null || discountNode.isNull())) {
            return ServerResponse.failure("Missing required field: discountPrice (or discount_price) when product is on sale");
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
        return ServerResponse.success("Product added successfully", insertedProduct);
    }
}
