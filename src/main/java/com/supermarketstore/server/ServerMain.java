package com.supermarketstore.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarketstore.department.DepartmentDao;
import com.supermarketstore.department.JdbcDepartmentDao;
import com.supermarketstore.product.JdbcProductDao;
import com.supermarketstore.product.ProductDao;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.ServerResponse;
import com.supermarketstore.server.routing.RequestRouter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    // === Static Fields ===
    private static final int PORT = 9000;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket_store_system";
    private static final String DB_USER = "root";
    private static final String DB_PASS = System.getenv("TEST_DB_PASS");
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ExecutorService CLIENT_POOL = Executors.newCachedThreadPool();

    // === Methods ===

    /**
     * Starts the supermarket server.
     *
     * @throws IOException           if a socket or I/O error occurs during server startup
     *                               or client communication
     * @throws IllegalStateException if the TEST_DB_PASS environment variable is
     *                               missing or blank
     */
    public static void main(String[] args) throws IOException {
        validateConfiguration();
        RequestRouter router = createRouter();
        start(router);
    }

    /**
     * Validates that the database password is available in the environment.
     *
     * @throws IllegalStateException if the TEST_DB_PASS environment variable is
     *                               missing or blank
     */
    private static void validateConfiguration() {
        if (DB_PASS == null || DB_PASS.isBlank())
            throw new IllegalStateException("Set TEST_DB_PASS before running ServerMain");
    }

    /**
     * Creates the request router with JDBC-based department and product DAOs.
     *
     * @return a configured router for incoming client requests
     */
    private static RequestRouter createRouter() {
        DepartmentDao departmentDao = new JdbcDepartmentDao(DB_URL, DB_USER, DB_PASS);
        ProductDao productDao = new JdbcProductDao(DB_URL, DB_USER, DB_PASS);
        return new RequestRouter(departmentDao, productDao);
    }

    /**
     * Starts the accept loop and submits each connected client to the thread pool.
     *
     * @param router the router used to process incoming requests
     * @throws IOException if the server socket cannot be opened or used
     */
    private static void start(RequestRouter router) throws IOException {
        System.out.println("Server listening on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                CLIENT_POOL.submit(() -> {
                    try {
                        handleClient(clientSocket, router);
                    } catch (IOException e) {
                        System.out.println("Client handling error: " + e.getMessage());
                    }
                });
            }
        }
    }

    /**
     * Handles communication with a connected client.
     * Reads JSON requests line by line, routes each request, and writes
     * the corresponding JSON response back to the client.
     *
     * @param clientSocket the connected client socket
     * @param router       the router used to process incoming requests
     * @throws IOException if reading from or writing to the client fails
     */
    private static void handleClient(Socket clientSocket, RequestRouter router) throws IOException {
        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line);

                ServerResponse<?> response;

                try {
                    // Parse the incoming request, create object from line, and route it.
                    ClientRequest request = MAPPER.readValue(line, ClientRequest.class);
                    response = router.route(request);
                } catch (Exception e) {
                    response = ServerResponse.error("Invalid request: " + e.getMessage());
                }

                // Send the response back on one line.
                out.println(MAPPER.writeValueAsString(response));
            }

            System.out.println("Client disconnected");
        }
    }
}
