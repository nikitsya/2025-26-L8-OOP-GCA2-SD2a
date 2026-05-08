package com.supermarketstore.server.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.supermarketstore.department.Department;
import com.supermarketstore.department.DepartmentDao;
import com.supermarketstore.product.Product;
import com.supermarketstore.product.ProductDao;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.RequestType;
import com.supermarketstore.protocol.ServerResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link RequestRouter}.
 */
class RequestRouterTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private DepartmentDao emptyDepartmentDao() {
        return new DepartmentDao() {
            @Override
            public List<Department> getAllDepartments() {
                return List.of();
            }

            @Override
            public Optional<Department> getDepartmentById(int id) {
                return Optional.empty();
            }

            @Override
            public boolean deleteDepartmentById(int id) {
                return false;
            }

            @Override
            public Department insertDepartment(Department department) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Department updateDepartment(int id, Department department) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<Department> findDepartmentsByFilter(Predicate<Department> filter) {
                return List.of();
            }

            @Override
            public Optional<Department> getDepartmentImageById(int id) {
                return Optional.empty();
            }
        };
    }

    private ProductDao emptyProductDao() {
        return new ProductDao() {
            @Override
            public List<Product> getAllProducts() {
                return List.of();
            }

            @Override
            public Optional<Product> getProductById(int id) {
                return Optional.empty();
            }

            @Override
            public Optional<Product> getProductImageById(int id) {
                return Optional.empty();
            }

            @Override
            public boolean deleteProductById(int id) {
                return false;
            }

            @Override
            public Product insertProduct(Product product) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Product updateProduct(int id, Product product) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<Product> findProductsByFilter(Predicate<Product> filter) {
                return List.of();
            }
        };
    }

    @Test
    void route_whenGetDepartmentByIdRequestIsMissingId_returnsErrorResponse() {
        RequestRouter router = new RequestRouter(emptyDepartmentDao(), emptyProductDao());

        ObjectNode payload = mapper.createObjectNode();
        ClientRequest request = new ClientRequest(RequestType.GET_DEPARTMENT_BY_ID.name(), payload);

        ServerResponse<?> response = router.route(request);

        assertEquals("ERROR", response.getStatus());
        assertEquals("Missing required field: id", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void route_whenGetDepartmentByIdRequestIsValidAndDepartmentExists_returnsOkResponse() {
        Department expectedDepartment = new Department(1, "Bakery", 0, 2, 12000.0, 5, false);

        DepartmentDao departmentDao = new DepartmentDao() {
            @Override
            public List<Department> getAllDepartments() {
                return List.of();
            }

            @Override
            public Optional<Department> getDepartmentById(int id) {
                return id == 1 ? Optional.of(expectedDepartment) : Optional.empty();
            }

            @Override
            public boolean deleteDepartmentById(int id) {
                return false;
            }

            @Override
            public Department insertDepartment(Department department) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Department updateDepartment(int id, Department department) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<Department> findDepartmentsByFilter(Predicate<Department> filter) {
                return List.of();
            }

            @Override
            public Optional<Department> getDepartmentImageById(int id) {
                return Optional.empty();
            }
        };

        RequestRouter router = new RequestRouter(departmentDao, emptyProductDao());

        ObjectNode payload = mapper.createObjectNode();
        payload.put("id", 1);
        ClientRequest request = new ClientRequest(RequestType.GET_DEPARTMENT_BY_ID.name(), payload);

        ServerResponse<?> response = router.route(request);

        assertEquals("OK", response.getStatus());
        assertEquals("Department retrieved successfully", response.getMessage());
        assertEquals(expectedDepartment, response.getData());
    }

    @Test
    void route_whenGetAllDepartmentsRequestIsSent_returnsOkResponse() {
        Department first = new Department(1, "Bakery", 0, 2, 12000.0, 5, false);
        Department second = new Department(2, "Frozen Foods", 1, 5, 20000.0, 7, true);

        DepartmentDao departmentDao = new DepartmentDao() {
            @Override
            public List<Department> getAllDepartments() {
                return List.of(first, second);
            }

            @Override
            public Optional<Department> getDepartmentById(int id) {
                return Optional.empty();
            }

            @Override
            public boolean deleteDepartmentById(int id) {
                return false;
            }

            @Override
            public Department insertDepartment(Department department) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Department updateDepartment(int id, Department department) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<Department> findDepartmentsByFilter(Predicate<Department> filter) {
                return List.of();
            }

            @Override
            public Optional<Department> getDepartmentImageById(int id) {
                return Optional.empty();
            }
        };

        RequestRouter router = new RequestRouter(departmentDao, emptyProductDao());

        ClientRequest request = new ClientRequest(RequestType.GET_ALL_DEPARTMENTS.name(), null);

        ServerResponse<?> response = router.route(request);

        assertEquals("OK", response.getStatus());
        assertEquals("Departments retrieved successfully", response.getMessage());
        assertEquals(List.of(first, second), response.getData());
    }

    @Test
    void route_whenRequestTypeIsUnknown_returnsErrorResponse() {
        RequestRouter router = new RequestRouter(emptyDepartmentDao(), emptyProductDao());

        ClientRequest request = new ClientRequest("NOT_A_REAL_REQUEST", null);

        ServerResponse<?> response = router.route(request);

        assertEquals("ERROR", response.getStatus());
        assertEquals("Unknown request type: NOT_A_REAL_REQUEST", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void route_whenRequestIsNull_returnsErrorResponse() {
        RequestRouter router = new RequestRouter(emptyDepartmentDao(), emptyProductDao());

        ServerResponse<?> response = router.route(null);

        assertEquals("ERROR", response.getStatus());
        assertEquals("Request must not be null", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void route_whenDeleteDepartmentByIdRequestIsMissingId_returnsErrorResponse() {
        RequestRouter router = new RequestRouter(emptyDepartmentDao(), emptyProductDao());

        ObjectNode payload = mapper.createObjectNode();
        ClientRequest request = new ClientRequest(RequestType.DELETE_DEPARTMENT_BY_ID.name(), payload);

        ServerResponse<?> response = router.route(request);

        assertEquals("ERROR", response.getStatus());
        assertEquals("Missing required field: id", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void route_whenGetProductByIdRequestIsMissingId_returnsErrorResponse() {
        RequestRouter router = new RequestRouter(emptyDepartmentDao(), emptyProductDao());

        ObjectNode payload = mapper.createObjectNode();
        ClientRequest request = new ClientRequest(RequestType.GET_PRODUCT_BY_ID.name(), payload);

        ServerResponse<?> response = router.route(request);

        assertEquals("ERROR", response.getStatus());
        assertEquals("Missing required field: id", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void route_whenDisconnectRequestIsSent_returnsOkResponse() {
        RequestRouter router = new RequestRouter(emptyDepartmentDao(), emptyProductDao());

        ClientRequest request = new ClientRequest(RequestType.DISCONNECT.name(), null);

        ServerResponse<?> response = router.route(request);

        assertEquals("OK", response.getStatus());
        assertEquals("Client disconnected successfully", response.getMessage());
        assertNull(response.getData());
    }
}
