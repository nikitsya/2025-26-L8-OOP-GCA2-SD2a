package com.supermarketstore.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarketstore.department.Department;
import com.supermarketstore.department.DepartmentDao;
import com.supermarketstore.product.Product;
import com.supermarketstore.product.ProductDao;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.RequestType;
import com.supermarketstore.server.routing.RequestRouter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the client handling loop in {@link ServerMain}.
 */
class ServerMainTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void handleClient_writesResponsesForInvalidJsonValidRequestAndDisconnect() throws Exception {
        RequestRouter router = new RequestRouter(emptyDepartmentDao(), productDaoWithProducts());
        String input = String.join(System.lineSeparator(),
                "{not-json",
                MAPPER.writeValueAsString(new ClientRequest(RequestType.GET_ALL_PRODUCTS.name(), null)),
                MAPPER.writeValueAsString(new ClientRequest(RequestType.DISCONNECT.name(), null))
        ) + System.lineSeparator();
        FakeSocket socket = new FakeSocket(input);

        Method method = ServerMain.class.getDeclaredMethod("handleClient", Socket.class, RequestRouter.class);
        method.setAccessible(true);
        method.invoke(null, socket, router);

        List<JsonNode> responses = Arrays.stream(socket.output().split("\\R"))
                .filter(line -> !line.isBlank())
                .map(ServerMainTest::readJson)
                .toList();

        assertEquals(3, responses.size());
        assertEquals("ERROR", responses.get(0).get("status").asText());
        assertEquals("OK", responses.get(1).get("status").asText());
        assertEquals("Products retrieved successfully", responses.get(1).get("message").asText());
        assertEquals("OK", responses.get(2).get("status").asText());
        assertEquals("Client disconnected successfully", responses.get(2).get("message").asText());
    }

    private static JsonNode readJson(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static DepartmentDao emptyDepartmentDao() {
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

    private static ProductDao productDaoWithProducts() {
        Product product = new Product(1, "Bread", 1.20, false, null, 25, null, null, null, 0);
        return new ProductDao() {
            @Override
            public List<Product> getAllProducts() {
                return List.of(product);
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

    private static class FakeSocket extends Socket {
        private final ByteArrayInputStream input;
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();

        private FakeSocket(String input) {
            this.input = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public InputStream getInputStream() {
            return input;
        }

        @Override
        public OutputStream getOutputStream() {
            return output;
        }

        private String output() {
            return output.toString(StandardCharsets.UTF_8);
        }
    }
}
