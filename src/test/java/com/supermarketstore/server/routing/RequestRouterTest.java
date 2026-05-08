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

    @Test
    void route_whenGetDepartmentByIdRequestIsMissingId_returnsErrorResponse() {
        DepartmentDao departmentDao = new DepartmentDao() {
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

        ProductDao productDao = new ProductDao() {
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

        RequestRouter router = new RequestRouter(departmentDao, productDao);

        ObjectNode payload = mapper.createObjectNode();
        ClientRequest request = new ClientRequest(RequestType.GET_DEPARTMENT_BY_ID.name(), payload);

        ServerResponse<?> response = router.route(request);

        assertEquals("ERROR", response.getStatus());
        assertEquals("Missing required field: id", response.getMessage());
        assertNull(response.getData());
    }
}
