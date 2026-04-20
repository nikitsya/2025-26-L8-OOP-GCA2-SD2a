package com.supermarketstore.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.supermarketstore.department.Department;
import com.supermarketstore.product.Product;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.RequestType;
import com.supermarketstore.protocol.ServerResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class ClientMain {
    // === Static Fields ===
    private static final String HOST = "localhost";
    private static final int PORT = 9000;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // === Methods ===

    /**
     * Runs the supermarket client demo.
     *
     * @param args command-line arguments
     * @throws IOException if client-server communication fails
     */
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            runDepartmentDemo(out, in);
            runProductDemo(out, in);
        }
    }

    /**
     * Runs the department client demo flow.
     * Requests all departments, fetches one department by id,
     * inserts a new department, verifies it, updates it,
     * verifies the update, deletes it, and confirms removal.
     *
     * @param out the socket writer used to send requests
     * @param in  the socket reader used to receive responses
     * @throws IOException if client-server communication fails
     */
    private static void runDepartmentDemo(PrintWriter out, BufferedReader in) throws IOException {
        requestAllEntities(
                out, in,
                "Requesting all departments...",
                RequestType.GET_ALL_DEPARTMENTS,
                new TypeReference<ServerResponse<List<Department>>>() {
                }
        );

        requestEntityById(
                out, in,
                1,
                "Requesting one department by id...",
                RequestType.GET_DEPARTMENT_BY_ID,
                new TypeReference<ServerResponse<Department>>() {
                }
        );

        Department addedDepartment = addDemoDepartment(out, in);
        if (addedDepartment == null) {
            return;
        }

        int departmentId = addedDepartment.getDepartmentId();

        requestEntityById(
                out, in,
                departmentId,
                "Verifying the inserted department by id...",
                RequestType.GET_DEPARTMENT_BY_ID,
                new TypeReference<ServerResponse<Department>>() {
                }
        );

        updateDemoDepartment(out, in, departmentId);

        requestEntityById(
                out, in,
                departmentId,
                "Verifying the updated department by id...",
                RequestType.GET_DEPARTMENT_BY_ID,
                new TypeReference<ServerResponse<Department>>() {
                }
        );

        deleteEntityById(
                out, in,
                departmentId,
                "Deleting the updated department by id...",
                RequestType.DELETE_DEPARTMENT_BY_ID
        );

        requestEntityById(
                out, in,
                departmentId,
                "Verifying the deleted department by id...",
                RequestType.GET_DEPARTMENT_BY_ID,
                new TypeReference<ServerResponse<Department>>() {
                }
        );
    }

    /**
     * Runs the product client demo flow.
     * Requests all products, fetches one product by id,
     * inserts a new product, verifies it, updates it,
     * verifies the update, deletes it, and confirms removal.
     *
     * @param out the socket writer used to send requests
     * @param in  the socket reader used to receive responses
     * @throws IOException if client-server communication fails
     */
    private static void runProductDemo(PrintWriter out, BufferedReader in) throws IOException {
        requestAllEntities(
                out, in, "Requesting all products...",
                RequestType.GET_ALL_PRODUCTS,
                new TypeReference<ServerResponse<List<Product>>>() {
                }
        );
        requestEntityById(
                out, in, 1,
                "Requesting one product by id...",
                RequestType.GET_PRODUCT_BY_ID,
                new TypeReference<ServerResponse<Product>>() {
                }
        );
        Product addedProduct = addDemoProduct(out, in);

        if (addedProduct == null) {
            return;
        }

        int productId = addedProduct.getProductId();

        requestEntityById(
                out, in, productId,
                "Requesting one product by id...",
                RequestType.GET_PRODUCT_BY_ID,
                new TypeReference<ServerResponse<Product>>() {
                }
        );
        updateDemoProduct(out, in, productId);
        requestEntityById(
                out, in, productId,
                "Requesting one product by id...",
                RequestType.GET_PRODUCT_BY_ID,
                new TypeReference<ServerResponse<Product>>() {
                }
        );
        deleteEntityById(
                out, in,
                productId,
                "Deleting the updated product by id...",
                RequestType.DELETE_PRODUCT_BY_ID
        );
        requestEntityById(
                out, in, productId,
                "Requesting one product by id...",
                RequestType.GET_PRODUCT_BY_ID,
                new TypeReference<ServerResponse<Product>>() {
                }
        );
    }

    // === Helpers ===

    /**
     * Sends a typed request to the server and deserializes the JSON response.
     *
     * @param out          the socket writer used to send the serialized request
     * @param in           the socket reader used to receive the response line
     * @param type         the protocol request type to send
     * @param payload      the optional JSON payload, or null when no payload is required
     * @param responseType the Jackson type reference used to deserialize the typed server response
     * @return the deserialized server response for the request
     * @throws IOException if the request cannot be written or the response cannot be read or parsed
     */
    private static <T> ServerResponse<T> sendRequest(PrintWriter out, BufferedReader in, RequestType type, JsonNode payload, TypeReference<ServerResponse<T>> responseType) throws IOException {
        ClientRequest request = new ClientRequest(type.name(), payload);
        out.println(MAPPER.writeValueAsString(request));

        String line = in.readLine();
        return MAPPER.readValue(line, responseType);
    }

    /**
     * Requests all entities of the given type and prints the server response and returned items.
     *
     * @param out          the socket writer used to send requests
     * @param in           the socket reader used to receive responses
     * @param title        the message printed before sending the request
     * @param requestType  the request type used to fetch all entities
     * @param responseType the type reference used to deserialize the response body
     * @param <T>          the entity type returned by the server
     * @throws IOException if client-server communication fails
     */
    private static <T> void requestAllEntities(PrintWriter out, BufferedReader in, String title, RequestType requestType, TypeReference<ServerResponse<List<T>>> responseType) throws IOException {
        System.out.println();
        System.out.println(title);

        ServerResponse<List<T>> response = sendRequest(out, in, requestType, null, responseType);
        printResponse(response);

        List<T> items = response.getData();
        if (items != null) {
            for (T item : items) {
                System.out.println(item);
            }
        }
    }

    /**
     * Requests one entity by id and prints the server response and returned entity.
     *
     * @param out          the socket writer used to send requests
     * @param in           the socket reader used to receive responses
     * @param id           the entity id
     * @param title        the message printed before sending the request
     * @param requestType  the request type used to fetch the entity
     * @param responseType the type reference used to deserialize the response body
     * @param <T>          the entity type returned by the server
     * @throws IOException if client-server communication fails
     */
    private static <T> void requestEntityById(PrintWriter out, BufferedReader in, int id, String title, RequestType requestType, TypeReference<ServerResponse<T>> responseType) throws IOException {
        System.out.println();
        System.out.println(title);

        ObjectNode payload = MAPPER.createObjectNode();
        payload.put("id", id);

        ServerResponse<T> response = sendRequest(out, in, requestType, payload, responseType);
        printResponse(response);

        T entity = response.getData();
        if (entity != null) {
            System.out.println(entity);
        }
    }

    /**
     * Deletes one entity by id and prints the server response.
     *
     * @param out         the socket writer used to send requests
     * @param in          the socket reader used to receive responses
     * @param id          the entity id
     * @param title       the message printed before sending the request
     * @param requestType the request type used to delete the entity
     * @throws IOException if client-server communication fails
     */
    private static void deleteEntityById(PrintWriter out, BufferedReader in, int id, String title, RequestType requestType) throws IOException {
        System.out.println();
        System.out.println(title);

        ObjectNode payload = MAPPER.createObjectNode();
        payload.put("id", id);

        ServerResponse<Void> response = sendRequest(
                out, in,
                requestType, payload,
                new TypeReference<>() {
                }
        );
        printResponse(response);
    }

    private static void printResponse(ServerResponse<?> response) {
        System.out.println("Status: " + response.getStatus());
        System.out.println("Message: " + response.getMessage());
    }

    // === Department Helpers ===

    private static Department addDemoDepartment(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println();
        System.out.println("Adding a new department...");

        ObjectNode addDepartmentPayload = MAPPER.createObjectNode();
        addDepartmentPayload.put("name", "TEST_NewDepartment");
        addDepartmentPayload.put("floor", 1);
        addDepartmentPayload.put("zone", 11);
        addDepartmentPayload.put("budget", 10000.0);
        addDepartmentPayload.put("employeeCount", 5);
        addDepartmentPayload.put("isRefrigerated", false);

        ServerResponse<Department> addDepartmentResponse = sendRequest(
                out, in,
                RequestType.ADD_DEPARTMENT, addDepartmentPayload,
                new TypeReference<>() {
                }
        );
        printResponse(addDepartmentResponse);

        Department addedDepartment = addDepartmentResponse.getData();
        if (addedDepartment != null) {
            System.out.println(addedDepartment);
        }
        return addedDepartment;
    }

    private static void updateDemoDepartment(PrintWriter out, BufferedReader in, int departmentId) throws IOException {
        System.out.println();
        System.out.println("Updating the department by id...");

        ObjectNode updateDepartmentPayload = MAPPER.createObjectNode();
        updateDepartmentPayload.put("id", departmentId);
        updateDepartmentPayload.put("name", "TEST_UpdatedDepartment");
        updateDepartmentPayload.put("floor", 2);
        updateDepartmentPayload.put("zone", 12);
        updateDepartmentPayload.put("budget", 15000.0);
        updateDepartmentPayload.put("employeeCount", 8);
        updateDepartmentPayload.put("isRefrigerated", true);

        ServerResponse<Department> updateDepartmentResponse = sendRequest(
                out, in,
                RequestType.UPDATE_DEPARTMENT, updateDepartmentPayload,
                new TypeReference<>() {
                }
        );
        printResponse(updateDepartmentResponse);

        Department updatedDepartment = updateDepartmentResponse.getData();
        if (updatedDepartment != null) {
            System.out.println(updatedDepartment);
        }
    }

    // === Product Helpers ===

    private static Product addDemoProduct(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println();
        System.out.println("Adding a new product...");
        ObjectNode addProductPayload = MAPPER.createObjectNode();
        addProductPayload.put("name", "TEST_NewProduct");
        addProductPayload.put("price", 29.99);
        addProductPayload.put("isOnSale", true);
        addProductPayload.put("discountPrice", 19.99);
        addProductPayload.put("stock", 50);
        ServerResponse<Product> addProductResponse = sendRequest(
                out, in,
                RequestType.ADD_PRODUCT, addProductPayload,
                new TypeReference<>() {
                }
        );
        printResponse(addProductResponse);
        Product addedProduct = addProductResponse.getData();
        if (addedProduct != null) {
            System.out.println(addedProduct);
        }
        return addedProduct;
    }

    private static void updateDemoProduct(PrintWriter out, BufferedReader in, int productId) throws IOException {
        System.out.println();
        System.out.println("Updating the product by id...");
        ObjectNode updateProductPayload = MAPPER.createObjectNode();
        updateProductPayload.put("id", productId);
        updateProductPayload.put("name", "TEST_UpdatedProduct");
        updateProductPayload.put("price", 24.99);
        updateProductPayload.put("isOnSale", true);
        updateProductPayload.put("discountPrice", 17.49);
        updateProductPayload.put("stock", 35);
        ServerResponse<Product> updateProductResponse = sendRequest(
                out, in,
                RequestType.UPDATE_PRODUCT, updateProductPayload,
                new TypeReference<>() {
                }
        );
        printResponse(updateProductResponse);
        Product updatedProduct = updateProductResponse.getData();
        if (updatedProduct != null) {
            System.out.println(updatedProduct);
        }
    }
}
