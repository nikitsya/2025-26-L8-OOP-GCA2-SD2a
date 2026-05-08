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
import java.util.Base64;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
    void route_whenGetAllProductsRequestIsSent_returnsOkResponse() {
        Product first = new Product(1, "Cola", 2.50, false, null, 40, new byte[]{1, 2, 3}, "cola.jpg", "image/jpeg", 3);
        Product second = new Product(2, "Bread", 1.20, false, null, 25, new byte[]{4, 5, 6, 7}, "bread.jpg", "image/jpeg", 4);

        ProductDao productDao = new ProductDao() {
            @Override
            public List<Product> getAllProducts() {
                return List.of(first, second);
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

        RequestRouter router = new RequestRouter(emptyDepartmentDao(), productDao);

        ClientRequest request = new ClientRequest(RequestType.GET_ALL_PRODUCTS.name(), null);

        ServerResponse<?> response = router.route(request);

        assertEquals("OK", response.getStatus());
        assertEquals("Products retrieved successfully", response.getMessage());
        assertEquals(List.of(first, second), response.getData());
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

    @Test
    void route_whenAddDepartmentRequestHasFileData_returnsInsertedDepartment() {
        CapturingDepartmentDao departmentDao = new CapturingDepartmentDao();
        RequestRouter router = new RequestRouter(departmentDao, emptyProductDao());
        byte[] image = new byte[]{1, 2, 3};

        ObjectNode payload = mapper.createObjectNode();
        payload.put("name", "Fresh Produce");
        payload.put("floor", 1);
        payload.put("zone", 4);
        payload.put("budget", 25000.0);
        payload.put("employeeCount", 9);
        payload.put("isRefrigerated", true);
        payload.put("fileData", Base64.getEncoder().encodeToString(image));
        payload.put("fileName", "produce.png");
        payload.put("contentType", "image/png");
        payload.put("fileSize", image.length);

        ServerResponse<?> response = router.route(new ClientRequest(RequestType.ADD_DEPARTMENT.name(), payload));

        assertEquals("OK", response.getStatus());
        assertEquals("Department added successfully", response.getMessage());
        Department inserted = (Department) response.getData();
        assertEquals(99, inserted.getDepartmentId());
        assertEquals("Fresh Produce", departmentDao.inserted.getName());
        assertArrayEquals(image, departmentDao.inserted.getDepartmentImage());
    }

    @Test
    void route_whenUpdateDepartmentRequestUsesSnakeCaseFileFields_returnsUpdatedDepartment() {
        CapturingDepartmentDao departmentDao = new CapturingDepartmentDao();
        departmentDao.existing = new Department(7, "Old", 1, 1, 100.0, 1, false);
        RequestRouter router = new RequestRouter(departmentDao, emptyProductDao());
        byte[] image = new byte[]{4, 5};

        ObjectNode payload = mapper.createObjectNode();
        payload.put("id", 7);
        payload.put("name", "Updated");
        payload.put("floor", 2);
        payload.put("zone", 8);
        payload.put("budget", 50000.0);
        payload.put("employeeCount", 12);
        payload.put("isRefrigerated", false);
        payload.put("file_data", Base64.getEncoder().encodeToString(image));
        payload.put("file_name", "updated.jpg");
        payload.put("content_type", "image/jpeg");
        payload.put("file_size", image.length);

        ServerResponse<?> response = router.route(new ClientRequest(RequestType.UPDATE_DEPARTMENT.name(), payload));

        assertEquals("OK", response.getStatus());
        assertEquals("Department updated successfully", response.getMessage());
        assertEquals(7, departmentDao.updatedId);
        assertEquals("Updated", departmentDao.updated.getName());
        assertEquals("updated.jpg", departmentDao.updated.getFileName());
        assertArrayEquals(image, departmentDao.updated.getDepartmentImage());
    }

    @Test
    void route_whenDepartmentImageAndDeleteRequestsMatchExistingDepartment_returnsOkResponses() {
        CapturingDepartmentDao departmentDao = new CapturingDepartmentDao();
        departmentDao.existing = new Department(3, "Bakery", 0, 2, 12000.0, 5, false, "bakery.png", "image/png", 2, new byte[]{9, 8});
        departmentDao.deleteResult = true;
        RequestRouter router = new RequestRouter(departmentDao, emptyProductDao());

        ObjectNode payload = mapper.createObjectNode();
        payload.put("id", 3);

        ServerResponse<?> imageResponse = router.route(new ClientRequest(RequestType.GET_DEPARTMENT_IMAGE_BY_ID.name(), payload));
        ServerResponse<?> deleteResponse = router.route(new ClientRequest(RequestType.DELETE_DEPARTMENT_BY_ID.name(), payload));

        assertEquals("OK", imageResponse.getStatus());
        assertEquals("Department image retrieved successfully", imageResponse.getMessage());
        assertEquals(departmentDao.existing, imageResponse.getData());
        assertEquals("OK", deleteResponse.getStatus());
        assertEquals("Department deleted successfully", deleteResponse.getMessage());
    }

    @Test
    void route_whenAddProductRequestHasFileData_returnsInsertedProduct() {
        CapturingProductDao productDao = new CapturingProductDao();
        RequestRouter router = new RequestRouter(emptyDepartmentDao(), productDao);
        byte[] image = new byte[]{6, 7, 8};

        ObjectNode payload = mapper.createObjectNode();
        payload.put("name", "Coffee");
        payload.put("price", 4.50);
        payload.put("isOnSale", true);
        payload.put("discountPrice", 3.75);
        payload.put("stock", 30);
        payload.put("fileData", Base64.getEncoder().encodeToString(image));
        payload.put("fileName", "coffee.jpg");
        payload.put("contentType", "image/jpeg");
        payload.put("fileSize", image.length);

        ServerResponse<?> response = router.route(new ClientRequest(RequestType.ADD_PRODUCT.name(), payload));

        assertEquals("OK", response.getStatus());
        assertEquals("Product added successfully", response.getMessage());
        Product inserted = (Product) response.getData();
        assertEquals(88, inserted.getProductId());
        assertEquals("Coffee", productDao.inserted.getName());
        assertArrayEquals(image, productDao.inserted.getProductImage());
    }

    @Test
    void route_whenUpdateProductRequestUsesSnakeCaseFields_returnsUpdatedProduct() {
        CapturingProductDao productDao = new CapturingProductDao();
        productDao.existing = new Product(4, "Old", 5.0, false, null, 4, null, null, null, 0);
        RequestRouter router = new RequestRouter(emptyDepartmentDao(), productDao);
        byte[] image = new byte[]{3, 2, 1};

        ObjectNode payload = mapper.createObjectNode();
        payload.put("id", 4);
        payload.put("name", "Tea");
        payload.put("price", 3.20);
        payload.put("is_on_sale", false);
        payload.put("stock", 50);
        payload.put("file_data", Base64.getEncoder().encodeToString(image));
        payload.put("file_name", "tea.jpg");
        payload.put("content_type", "image/jpeg");
        payload.put("file_size", image.length);

        ServerResponse<?> response = router.route(new ClientRequest(RequestType.UPDATE_PRODUCT.name(), payload));

        assertEquals("OK", response.getStatus());
        assertEquals("Product updated successfully", response.getMessage());
        assertEquals(4, productDao.updatedId);
        assertEquals("Tea", productDao.updated.getName());
        assertEquals("tea.jpg", productDao.updated.getFileName());
        assertArrayEquals(image, productDao.updated.getProductImage());
    }

    @Test
    void route_whenProductImageAndDeleteRequestsMatchExistingProduct_returnsOkResponses() {
        CapturingProductDao productDao = new CapturingProductDao();
        productDao.existing = new Product(5, "Bread", 1.20, false, null, 25, new byte[]{1}, "bread.jpg", "image/jpeg", 1);
        productDao.deleteResult = true;
        RequestRouter router = new RequestRouter(emptyDepartmentDao(), productDao);

        ObjectNode payload = mapper.createObjectNode();
        payload.put("id", 5);

        ServerResponse<?> imageResponse = router.route(new ClientRequest(RequestType.GET_PRODUCT_IMAGE_BY_ID.name(), payload));
        ServerResponse<?> deleteResponse = router.route(new ClientRequest(RequestType.DELETE_PRODUCT_BY_ID.name(), payload));

        assertEquals("OK", imageResponse.getStatus());
        assertEquals("Product image retrieved successfully", imageResponse.getMessage());
        assertEquals(productDao.existing, imageResponse.getData());
        assertEquals("OK", deleteResponse.getStatus());
        assertEquals("Product deleted successfully", deleteResponse.getMessage());
    }

    @Test
    void route_whenProductPayloadIsInvalid_returnsValidationErrors() {
        RequestRouter router = new RequestRouter(emptyDepartmentDao(), emptyProductDao());

        ObjectNode missingDiscount = mapper.createObjectNode();
        missingDiscount.put("name", "Coffee");
        missingDiscount.put("price", 4.50);
        missingDiscount.put("isOnSale", true);
        missingDiscount.put("stock", 30);

        ObjectNode invalidFileData = missingDiscount.deepCopy();
        invalidFileData.put("discountPrice", 3.75);
        invalidFileData.put("fileData", "***not-base64***");

        ServerResponse<?> missingDiscountResponse = router.route(new ClientRequest(RequestType.ADD_PRODUCT.name(), missingDiscount));
        ServerResponse<?> invalidFileResponse = router.route(new ClientRequest(RequestType.ADD_PRODUCT.name(), invalidFileData));

        assertEquals("ERROR", missingDiscountResponse.getStatus());
        assertEquals("Missing required field: discountPrice (or discount_price) when product is on sale", missingDiscountResponse.getMessage());
        assertEquals("ERROR", invalidFileResponse.getStatus());
        assertEquals("Invalid file data: expected binary (byte[]) content", invalidFileResponse.getMessage());
    }

    private static class CapturingDepartmentDao implements DepartmentDao {
        private Department existing;
        private Department inserted;
        private Department updated;
        private int updatedId;
        private boolean deleteResult;

        @Override
        public List<Department> getAllDepartments() {
            return existing == null ? List.of() : List.of(existing);
        }

        @Override
        public Optional<Department> getDepartmentById(int id) {
            return existing != null && existing.getDepartmentId() == id ? Optional.of(existing) : Optional.empty();
        }

        @Override
        public boolean deleteDepartmentById(int id) {
            return deleteResult;
        }

        @Override
        public Department insertDepartment(Department department) {
            inserted = department;
            return new Department(99, department.getName(), department.getFloor(), department.getZone(), department.getBudget(), department.getEmployeeCount(), department.isRefrigerated(), department.getFileName(), department.getContentType(), department.getFileSize(), department.getDepartmentImage());
        }

        @Override
        public Department updateDepartment(int id, Department department) {
            updatedId = id;
            updated = department;
            return department;
        }

        @Override
        public List<Department> findDepartmentsByFilter(Predicate<Department> filter) {
            return List.of();
        }

        @Override
        public Optional<Department> getDepartmentImageById(int id) {
            return getDepartmentById(id);
        }
    }

    private static class CapturingProductDao implements ProductDao {
        private Product existing;
        private Product inserted;
        private Product updated;
        private int updatedId;
        private boolean deleteResult;

        @Override
        public List<Product> getAllProducts() {
            return existing == null ? List.of() : List.of(existing);
        }

        @Override
        public Optional<Product> getProductById(int id) {
            return existing != null && existing.getProductId() == id ? Optional.of(existing) : Optional.empty();
        }

        @Override
        public Optional<Product> getProductImageById(int id) {
            return getProductById(id);
        }

        @Override
        public boolean deleteProductById(int id) {
            return deleteResult;
        }

        @Override
        public Product insertProduct(Product product) {
            inserted = product;
            return new Product(88, product.getName(), product.getPrice(), product.isOnSale(), product.getDiscountPrice(), product.getStock(), product.getProductImage(), product.getFileName(), product.getContentType(), product.getFileSize());
        }

        @Override
        public Product updateProduct(int id, Product product) {
            updatedId = id;
            updated = product;
            return product;
        }

        @Override
        public List<Product> findProductsByFilter(Predicate<Product> filter) {
            return List.of();
        }
    }
}
