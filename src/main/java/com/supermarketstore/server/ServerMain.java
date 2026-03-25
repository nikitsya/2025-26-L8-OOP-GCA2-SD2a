package com.supermarketstore.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarketstore.department.DepartmentDao;
import com.supermarketstore.department.JdbcDepartmentDao;
import com.supermarketstore.product.JdbcProductDao;
import com.supermarketstore.product.ProductDao;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.RequestType;
import com.supermarketstore.protocol.ServerResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class ServerMain {
    private static final int PORT = 9000;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket_store_system";
    private static final String DB_USER = "root";
    private static final String DB_PASS = System.getenv("TEST_DB_PASS");

    /** Shared JSON mapper used for request parsing and response serialization. */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Starts the supermarket server.
     *
     * @param args command-line arguments
     * @throws IOException           if a socket or I/O error occurs during server startup
     *                               or client communication
     * @throws IllegalStateException if the TEST_DB_PASS environment variable is
     *                               missing or blank
     */
    public static void main(String[] args) throws IOException {
        validateConfiguration();

        RequestRouter router = createRouter();

        System.out.println("Server listening on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            // accept() blocks until a client connects
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            handleClient(serverSocket, router);
        }
    }

    private static void validateConfiguration() {
        if (DB_PASS == null || DB_PASS.isBlank()) {
            throw new IllegalStateException("Set TEST_DB_PASS before running ServerMain");
        }
    }

    private static RequestRouter createRouter() {
        DepartmentDao departmentDao = new JdbcDepartmentDao(DB_URL, DB_USER, DB_PASS);
        ProductDao productDao = new JdbcProductDao(DB_URL, DB_USER, DB_PASS);
        return new RequestRouter(departmentDao, productDao);
    }

    private static void handleClient(Socket clientSocket, RequestRouter router) throws IOException {
        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line);

                // Parse the incoming request, create object from line, and route it
                ClientRequest request = MAPPER.readValue(line, ClientRequest.class);
                ServerResponse<?> response = router.route(request);

                // Send the response back on one line
                out.println(MAPPER.writeValueAsString(response));
            }

            System.out.println("Client disconnected");
        }
    }
}
