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
     * inserts a new department, and verifies the inserted record.
     *
     * @param out the socket writer used to send requests
     * @param in  the socket reader used to receive responses
     * @throws IOException if client-server communication fails
     * @author Hanna Bokariuk
     */
    private static void runDepartmentDemo(PrintWriter out, BufferedReader in) throws IOException {
        ClientRequest request = createRequest(RequestType.GET_ALL_DEPARTMENTS, null);
        out.println(MAPPER.writeValueAsString(request));

        // Read and parse the response
        String line = in.readLine();
        ServerResponse<List<Department>> response = MAPPER.readValue(
                line, new TypeReference<>() {
                }
        );

        System.out.println("Status:  " + response.getStatus());
        System.out.println("Message: " + response.getMessage());

        List<Department> departments = response.getData();

        if (departments != null) {
            for (Department department : departments) {
                System.out.println(department);
            }
        }
        System.out.println();
        System.out.println("Requesting one department by id...");

        ObjectNode payload = MAPPER.createObjectNode();
        payload.put("id", 1);

        ClientRequest byIdRequest = createRequest(RequestType.GET_DEPARTMENT_BY_ID, payload);
        out.println(MAPPER.writeValueAsString(byIdRequest));

        String byIdLine = in.readLine();

        ServerResponse<Department> byIdResponse = MAPPER.readValue(
                byIdLine,
                new TypeReference<>() {
                }
        );

        System.out.println("Status: " + byIdResponse.getStatus());
        System.out.println("Message: " + byIdResponse.getMessage());

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

        ClientRequest addRequest = createRequest(RequestType.ADD_DEPARTMENT, addPayload);
        out.println(MAPPER.writeValueAsString(addRequest));

        String addLine = in.readLine();

        ServerResponse<Department> addResponse = MAPPER.readValue(addLine, new TypeReference<>() {
                }
        );

        System.out.println("Status: " + addResponse.getStatus());
        System.out.println("Message: " + addResponse.getMessage());

        Department addedDepartment = addResponse.getData();

        if (addedDepartment != null) {
            System.out.println(addedDepartment);
        }
        if (addedDepartment != null) {
            System.out.println();
            System.out.println("Verifying the inserted department by id...");

            ObjectNode verifyPayload = MAPPER.createObjectNode();
            verifyPayload.put("id", addedDepartment.getDepartmentId());

            ClientRequest verifyRequest = createRequest(RequestType.GET_DEPARTMENT_BY_ID, verifyPayload);
            out.println(MAPPER.writeValueAsString(verifyRequest));

            String verifyLine = in.readLine();

            ServerResponse<Department> verifyResponse = MAPPER.readValue(
                    verifyLine,
                    new TypeReference<>() {
                    }
            );

            System.out.println("Status: " + verifyResponse.getStatus());
            System.out.println("Message: " + verifyResponse.getMessage());

            Department verifiedDepartment = verifyResponse.getData();

            if (verifiedDepartment != null) {
                System.out.println(verifiedDepartment);
            }
        }

        if (addedDepartment != null) {
            System.out.println();
            System.out.println("Deleting the inserted department by id...");

            ObjectNode deletePayload = MAPPER.createObjectNode();
            deletePayload.put("id", addedDepartment.getDepartmentId());

            ClientRequest deleteRequest = createRequest(RequestType.DELETE_DEPARTMENT_BY_ID, deletePayload);
            out.println(MAPPER.writeValueAsString(deleteRequest));

            String deleteLine = in.readLine();
            ServerResponse<?> deleteResponse = MAPPER.readValue(
                    deleteLine,
                    new TypeReference<>() {
                    }
            );

            System.out.println("Status: " + deleteResponse.getStatus());
            System.out.println("Message: " + deleteResponse.getMessage());
        }
    }

    /**
     * Runs the product client demo flow.
     * Requests all products, fetches one product by id,
     * inserts a new product, and verifies the inserted record.
     *
     * @param out the socket writer used to send requests
     * @param in  the socket reader used to receive responses
     * @throws IOException if client-server communication fails
     * @author Nikita Smiichyk
     */
    private static void runProductDemo(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println();
        System.out.println("Requesting all products...");

        ClientRequest productsRequest = createRequest(RequestType.GET_ALL_PRODUCTS, null);
        out.println(MAPPER.writeValueAsString(productsRequest));

        String productsLine = in.readLine();
        ServerResponse<List<Product>> productsResponse = MAPPER.readValue(
                productsLine,
                new TypeReference<>() {
                }
        );

        System.out.println("Status: " + productsResponse.getStatus());
        System.out.println("Message: " + productsResponse.getMessage());

        List<Product> products = productsResponse.getData();
        if (products != null) {
            for (Product product : products) {
                System.out.println(product);
            }
        }

        System.out.println();
        System.out.println("Requesting one product by id...");

        ObjectNode productByIdPayload = MAPPER.createObjectNode();
        productByIdPayload.put("id", 1);

        ClientRequest productByIdRequest = createRequest(RequestType.GET_PRODUCT_BY_ID, productByIdPayload);
        out.println(MAPPER.writeValueAsString(productByIdRequest));

        String productByIdLine = in.readLine();
        ServerResponse<Product> productByIdResponse = MAPPER.readValue(
                productByIdLine,
                new TypeReference<>() {
                }
        );

        System.out.println("Status: " + productByIdResponse.getStatus());
        System.out.println("Message: " + productByIdResponse.getMessage());

        Product productById = productByIdResponse.getData();
        if (productById != null) {
            System.out.println(productById);
        }

        System.out.println();
        System.out.println("Adding a new product...");

        ObjectNode addProductPayload = MAPPER.createObjectNode();
        addProductPayload.put("name", "TEST_NewProduct");
        addProductPayload.put("price", 29.99);
        addProductPayload.put("isOnSale", true);
        addProductPayload.put("discountPrice", 19.99);
        addProductPayload.put("stock", 50);

        ClientRequest addProductRequest = createRequest(RequestType.ADD_PRODUCT, addProductPayload);
        out.println(MAPPER.writeValueAsString(addProductRequest));

        String addProductLine = in.readLine();
        ServerResponse<Product> addProductResponse = MAPPER.readValue(
                addProductLine,
                new TypeReference<>() {
                }
        );

        System.out.println("Status: " + addProductResponse.getStatus());
        System.out.println("Message: " + addProductResponse.getMessage());

        Product addedProduct = addProductResponse.getData();
        if (addedProduct != null) {
            System.out.println(addedProduct);
        }

        if (addedProduct != null) {
            System.out.println();
            System.out.println("Verifying the inserted product by id...");

            ObjectNode verifyProductPayload = MAPPER.createObjectNode();
            verifyProductPayload.put("id", addedProduct.getProductId());

            ClientRequest verifyProductRequest = createRequest(RequestType.GET_PRODUCT_BY_ID, verifyProductPayload);
            out.println(MAPPER.writeValueAsString(verifyProductRequest));

            String verifyProductLine = in.readLine();
            ServerResponse<Product> verifyProductResponse = MAPPER.readValue(
                    verifyProductLine,
                    new TypeReference<>() {
                    }
            );

            System.out.println("Status: " + verifyProductResponse.getStatus());
            System.out.println("Message: " + verifyProductResponse.getMessage());

            Product verifiedProduct = verifyProductResponse.getData();
            if (verifiedProduct != null) {
                System.out.println(verifiedProduct);
            }
        }
    }

    /**
     * Creates a client request using the supplied protocol type and JSON payload.
     *
     * @param type    the protocol request type
     * @param payload the optional JSON payload, or null when no payload is needed
     * @return a populated ClientRequest ready to be serialized and sent
     * @author Nikita Smiichyk
     */
    private static ClientRequest createRequest(RequestType type, JsonNode payload) {
        return new ClientRequest(type.name(), payload);
    }
}
