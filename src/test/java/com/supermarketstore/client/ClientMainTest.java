package com.supermarketstore.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarketstore.department.Department;
import com.supermarketstore.product.Product;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.ServerResponse;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the socket request flow helpers in {@link ClientMain}.
 */
class ClientMainTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void runDepartmentDemo_sendsExpectedDepartmentRequests() throws Exception {
        StringWriter requestWriter = new StringWriter();
        BufferedReader responses = responses(
                ServerResponse.ok("departments found", List.of(department(1, "Bakery"))),
                ServerResponse.ok("department found", department(1, "Bakery")),
                ServerResponse.ok("department added", department(41, "TEST_NewDepartment")),
                ServerResponse.ok("department found", department(41, "TEST_NewDepartment")),
                ServerResponse.ok("department updated", department(41, "TEST_UpdatedDepartment")),
                ServerResponse.ok("department found", department(41, "TEST_UpdatedDepartment")),
                ServerResponse.ok("department image found", departmentWithImage(41, "TEST_UpdatedDepartment")),
                ServerResponse.ok("department deleted", null),
                ServerResponse.error("department not found")
        );

        invokeDemo("runDepartmentDemo", requestWriter, responses);

        assertEquals(
                List.of(
                        "GET_ALL_DEPARTMENTS",
                        "GET_DEPARTMENT_BY_ID",
                        "ADD_DEPARTMENT",
                        "GET_DEPARTMENT_BY_ID",
                        "UPDATE_DEPARTMENT",
                        "GET_DEPARTMENT_BY_ID",
                        "GET_DEPARTMENT_IMAGE_BY_ID",
                        "DELETE_DEPARTMENT_BY_ID",
                        "GET_DEPARTMENT_BY_ID"
                ),
                requestTypes(requestWriter)
        );
        List<JsonNode> payloads = requestPayloads(requestWriter);
        assertEquals(41, payloads.get(3).get("id").asInt());
        assertEquals("TEST_UpdatedDepartment", payloads.get(4).get("name").asText());
        assertTrue(payloads.get(4).get("isRefrigerated").asBoolean());
        assertEquals(41, payloads.get(7).get("id").asInt());
    }

    @Test
    void runDepartmentDemo_stopsWhenAddReturnsNoDepartment() throws Exception {
        StringWriter requestWriter = new StringWriter();
        BufferedReader responses = responses(
                ServerResponse.ok("departments found", List.of(department(1, "Bakery"))),
                ServerResponse.ok("department found", department(1, "Bakery")),
                ServerResponse.error("department not added")
        );

        invokeDemo("runDepartmentDemo", requestWriter, responses);

        assertEquals(
                List.of("GET_ALL_DEPARTMENTS", "GET_DEPARTMENT_BY_ID", "ADD_DEPARTMENT"),
                requestTypes(requestWriter)
        );
    }

    @Test
    void runProductDemo_sendsExpectedProductRequests() throws Exception {
        StringWriter requestWriter = new StringWriter();
        BufferedReader responses = responses(
                ServerResponse.ok("products found", List.of(product(1, "Potatoes"))),
                ServerResponse.ok("product found", product(1, "Potatoes")),
                ServerResponse.ok("product added", product(52, "TEST_NewProduct")),
                ServerResponse.ok("product found", product(52, "TEST_NewProduct")),
                ServerResponse.ok("product updated", product(52, "TEST_UpdatedProduct")),
                ServerResponse.ok("product found", product(52, "TEST_UpdatedProduct")),
                ServerResponse.ok("product image found", productWithImage(52, "TEST_UpdatedProduct")),
                ServerResponse.ok("product deleted", null),
                ServerResponse.error("product not found")
        );

        invokeDemo("runProductDemo", requestWriter, responses);

        assertEquals(
                List.of(
                        "GET_ALL_PRODUCTS",
                        "GET_PRODUCT_BY_ID",
                        "ADD_PRODUCT",
                        "GET_PRODUCT_BY_ID",
                        "UPDATE_PRODUCT",
                        "GET_PRODUCT_BY_ID",
                        "GET_PRODUCT_IMAGE_BY_ID",
                        "DELETE_PRODUCT_BY_ID",
                        "GET_PRODUCT_BY_ID"
                ),
                requestTypes(requestWriter)
        );
        List<JsonNode> payloads = requestPayloads(requestWriter);
        assertEquals(52, payloads.get(3).get("id").asInt());
        assertEquals("TEST_UpdatedProduct", payloads.get(4).get("name").asText());
        assertTrue(payloads.get(4).get("isOnSale").asBoolean());
        assertEquals(52, payloads.get(7).get("id").asInt());
    }

    @Test
    void runProductDemo_stopsWhenAddReturnsNoProduct() throws Exception {
        StringWriter requestWriter = new StringWriter();
        BufferedReader responses = responses(
                ServerResponse.ok("products found", List.of(product(1, "Potatoes"))),
                ServerResponse.ok("product found", product(1, "Potatoes")),
                ServerResponse.error("product not added")
        );

        invokeDemo("runProductDemo", requestWriter, responses);

        assertEquals(
                List.of("GET_ALL_PRODUCTS", "GET_PRODUCT_BY_ID", "ADD_PRODUCT"),
                requestTypes(requestWriter)
        );
    }

    @Test
    void disconnectClient_sendsDisconnectRequest() throws Exception {
        StringWriter requestWriter = new StringWriter();
        BufferedReader responses = responses(ServerResponse.ok("bye", null));

        Method method = ClientMain.class.getDeclaredMethod("disconnectClient", PrintWriter.class, BufferedReader.class);
        method.setAccessible(true);
        method.invoke(null, new PrintWriter(requestWriter, true), responses);

        assertEquals(List.of("DISCONNECT"), requestTypes(requestWriter));
    }

    @Test
    void saveRetrievedFile_ignoresMissingFileData() throws Exception {
        Method method = ClientMain.class.getDeclaredMethod(
                "saveRetrievedFile",
                java.nio.file.Path.class,
                String.class,
                String.class,
                byte[].class
        );
        method.setAccessible(true);

        method.invoke(null, java.nio.file.Path.of("target", "downloads", "ignored"), "Client file", "", "data".getBytes());
        method.invoke(null, java.nio.file.Path.of("target", "downloads", "ignored"), "Client file", "file.bin", null);

        assertFalse(java.nio.file.Files.exists(java.nio.file.Path.of("target", "downloads", "ignored", "file.bin")));
    }

    private static void invokeDemo(String methodName, StringWriter requestWriter, BufferedReader responses) throws Exception {
        Method method = ClientMain.class.getDeclaredMethod(methodName, PrintWriter.class, BufferedReader.class);
        method.setAccessible(true);
        method.invoke(null, new PrintWriter(requestWriter, true), responses);
    }

    private static BufferedReader responses(ServerResponse<?>... responses) {
        String responseLines = Arrays.stream(responses)
                .map(ClientMainTest::toJson)
                .collect(Collectors.joining(System.lineSeparator()));
        return new BufferedReader(new StringReader(responseLines));
    }

    private static String toJson(ServerResponse<?> response) {
        try {
            return MAPPER.writeValueAsString(response);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static List<String> requestTypes(StringWriter writer) {
        return Arrays.stream(writer.toString().split("\\R"))
                .filter(line -> !line.isBlank())
                .map(ClientMainTest::readRequest)
                .map(ClientRequest::getType)
                .toList();
    }

    private static List<JsonNode> requestPayloads(StringWriter writer) {
        return Arrays.stream(writer.toString().split("\\R"))
                .filter(line -> !line.isBlank())
                .map(ClientMainTest::readRequest)
                .map(ClientRequest::getPayload)
                .toList();
    }

    private static ClientRequest readRequest(String json) {
        try {
            return MAPPER.readValue(json, ClientRequest.class);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Department department(int id, String name) {
        return new Department(id, name, 1, 1, 1000.0, 5, false);
    }

    private static Department departmentWithImage(int id, String name) {
        return new Department(id, name, 1, 1, 1000.0, 5, false, "department.png", "image/png", 4, new byte[]{1, 2, 3, 4});
    }

    private static Product product(int id, String name) {
        return new Product(id, name, 10.0, true, 7.5, 20, null, null, null, 0);
    }

    private static Product productWithImage(int id, String name) {
        return new Product(id, name, 10.0, true, 7.5, 20, new byte[]{5, 6, 7}, "product.jpg", "image/jpeg", 3);
    }
}
