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
     * @author Hanna Bokariuk
     */
    private static void runDepartmentDemo(PrintWriter out, BufferedReader in) throws IOException {
        requestAllEntities(
                out, in, "Requesting all departments...",
                RequestType.GET_ALL_DEPARTMENTS,
                new TypeReference<ServerResponse<List<Department>>>() {
                }
        );

        System.out.println();
        System.out.println("Requesting one department by id...");
        ObjectNode payload = MAPPER.createObjectNode();
        payload.put("id", 1);
        ServerResponse<Department> byIdResponse = sendRequest(
                out, in,
                RequestType.GET_DEPARTMENT_BY_ID, payload,
                new TypeReference<>() {
                }
        );
        printResponse(byIdResponse);

        Department department = byIdResponse.getData();

        if (department != null) {
            System.out.println(department);
        }

        System.out.println();
        System.out.println("Adding a new department ");

        ObjectNode addPayload = MAPPER.createObjectNode();
        addPayload.put("name", "TEST_NewDepartment");
        addPayload.put("floor", 1);
        addPayload.put("zone", 11);
        addPayload.put("budget", 10000.0);
        addPayload.put("employeeCount", 5);
        addPayload.put("isRefrigerated", false);
        ServerResponse<Department> addResponse = sendRequest(
                out, in,
                RequestType.ADD_DEPARTMENT, addPayload,
                new TypeReference<>() {
                }
        );
        printResponse(addResponse);

        Department addedDepartment = addResponse.getData();

        if (addedDepartment != null) {
            System.out.println(addedDepartment);
        }
        if (addedDepartment != null) {
            System.out.println();
            System.out.println("Verifying the inserted department by id...");

            ObjectNode verifyPayload = MAPPER.createObjectNode();
            verifyPayload.put("id", addedDepartment.getDepartmentId());
            ServerResponse<Department> verifyResponse = sendRequest(
                    out, in,
                    RequestType.GET_DEPARTMENT_BY_ID, verifyPayload,
                    new TypeReference<>() {
                    }
            );
            printResponse(verifyResponse);

            Department verifiedDepartment = verifyResponse.getData();

            if (verifiedDepartment != null) {
                System.out.println(verifiedDepartment);
            }
        }

        if (addedDepartment != null) {
            System.out.println();
            System.out.println("Updating the inserted department...");

            ObjectNode updatePayload = MAPPER.createObjectNode();
            updatePayload.put("id", addedDepartment.getDepartmentId());
            updatePayload.put("name", "TEST_UpdatedDepartment");
            updatePayload.put("floor", 2);
            updatePayload.put("zone", 12);
            updatePayload.put("budget", 15000.0);
            updatePayload.put("employeeCount", 8);
            updatePayload.put("isRefrigerated", true);
            ServerResponse<Department> updateResponse = sendRequest(
                    out, in,
                    RequestType.UPDATE_DEPARTMENT, updatePayload,
                    new TypeReference<>() {
                    }
            );
            printResponse(updateResponse);

            Department updatedDepartment = updateResponse.getData();
            if (updatedDepartment != null) {
                System.out.println(updatedDepartment);
            }
        }
        if (addedDepartment != null) {
            System.out.println();
            System.out.println("Verifying the updated department by id...");

            ObjectNode verifyUpdatedPayload = MAPPER.createObjectNode();
            verifyUpdatedPayload.put("id", addedDepartment.getDepartmentId());
            ServerResponse<Department> verifyUpdatedResponse = sendRequest(
                    out, in,
                    RequestType.GET_DEPARTMENT_BY_ID, verifyUpdatedPayload,
                    new TypeReference<>() {
                    }
            );
            printResponse(verifyUpdatedResponse);

            Department verifiedUpdatedDepartment = verifyUpdatedResponse.getData();
            if (verifiedUpdatedDepartment != null) {
                System.out.println(verifiedUpdatedDepartment);
            }
        }

        if (addedDepartment != null) {
            System.out.println();
            System.out.println("Deleting the updated department by id...");

            ObjectNode deletePayload = MAPPER.createObjectNode();
            deletePayload.put("id", addedDepartment.getDepartmentId());
            ServerResponse<Void> deleteResponse = sendRequest(
                    out, in,
                    RequestType.DELETE_DEPARTMENT_BY_ID, deletePayload,
                    new TypeReference<>() {
                    }
            );
            printResponse(deleteResponse);

            System.out.println();
            System.out.println("Verifying the deleted department by id...");

            ObjectNode verifyDeletedPayload = MAPPER.createObjectNode();
            verifyDeletedPayload.put("id", addedDepartment.getDepartmentId());
            ServerResponse<Department> verifyDeletedResponse = sendRequest(
                    out, in,
                    RequestType.GET_DEPARTMENT_BY_ID, verifyDeletedPayload,
                    new TypeReference<>() {
                    }
            );
            printResponse(verifyDeletedResponse);

            Department deletedDepartmentCheck = verifyDeletedResponse.getData();
            if (deletedDepartmentCheck != null) {
                System.out.println(deletedDepartmentCheck);
            }
        }

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
     * @author Nikita Smiichyk
     */
    private static void runProductDemo(PrintWriter out, BufferedReader in) throws IOException {
        requestAllEntities(
                out, in, "Requesting all products...",
                RequestType.GET_ALL_PRODUCTS,
                new TypeReference<ServerResponse<List<Product>>>() {
                }
        );
        requestProductById(out, in, 1, "Requesting one product by id...");
        Product addedProduct = addDemoProduct(out, in);

        if (addedProduct == null) {
            return;
        }

        int productId = addedProduct.getProductId();

        requestProductById(out, in, productId, "Verifying the inserted product by id...");
        updateDemoProduct(out, in, productId, "Updating the product by id...");
        requestProductById(out, in, productId, "Verifying the updated product by id...");
        deleteProductById(out, in, productId, "Deleting the updated product by id...");
        requestProductById(out, in, productId, "Confirming that the product was deleted...");
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
     * @author Nikita Smiichyk
     */
    private static <T> ServerResponse<T> sendRequest(
            PrintWriter out, BufferedReader in,
            RequestType type, JsonNode payload,
            TypeReference<ServerResponse<T>> responseType
    ) throws IOException {
        ClientRequest request = new ClientRequest(type.name(), payload);
        out.println(MAPPER.writeValueAsString(request));

        String line = in.readLine();
        return MAPPER.readValue(line, responseType);
    }

    private static void printResponse(ServerResponse<?> response) {
        System.out.println("Status: " + response.getStatus());
        System.out.println("Message: " + response.getMessage());
    }

    private static <T> void requestAllEntities(
            PrintWriter out, BufferedReader in,
            String title, RequestType requestType,
            TypeReference<ServerResponse<List<T>>> responseType
    ) throws IOException {
        System.out.println();
        System.out.println(title);

        ServerResponse<List<T>> response = sendRequest(
                out, in,
                requestType, null,
                responseType
        );
        printResponse(response);

        List<T> items = response.getData();
        if (items != null) {
            for (T item : items) {
                System.out.println(item);
            }
        }
    }

    // === Product Helpers ===

    private static void requestProductById(PrintWriter out, BufferedReader in, int productId, String title) throws IOException {
        System.out.println();
        System.out.println(title);
        ObjectNode productByIdPayload = MAPPER.createObjectNode();
        productByIdPayload.put("id", productId);
        ServerResponse<Product> productByIdResponse = sendRequest(
                out, in,
                RequestType.GET_PRODUCT_BY_ID, productByIdPayload,
                new TypeReference<>() {
                }
        );
        printResponse(productByIdResponse);
        Product productById = productByIdResponse.getData();
        if (productById != null) {
            System.out.println(productById);
        }
    }

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

    private static void updateDemoProduct(PrintWriter out, BufferedReader in, int productId, String title) throws IOException {
        System.out.println();
        System.out.println(title);
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

    private static void deleteProductById(PrintWriter out, BufferedReader in, int productId, String title) throws IOException {
        System.out.println();
        System.out.println(title);
        ObjectNode deleteProductPayload = MAPPER.createObjectNode();
        deleteProductPayload.put("id", productId);
        ServerResponse<Void> deleteProductResponse = sendRequest(
                out, in,
                RequestType.DELETE_PRODUCT_BY_ID, deleteProductPayload,
                new TypeReference<>() {
                }
        );
        printResponse(deleteProductResponse);
    }
}
