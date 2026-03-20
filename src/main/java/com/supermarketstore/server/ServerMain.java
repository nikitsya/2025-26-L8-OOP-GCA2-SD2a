package com.supermarketstore.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.ServerResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import com.supermarketstore.department.Department;
import com.supermarketstore.department.JdbcDepartmentDao;


public class ServerMain {
    private static final int PORT = 9000;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket_store_system";
    private static final String DB_USER = "root";
    private static final String DB_PASS = System.getenv("TEST_DB_PASS");

    // Creates: a single shared mapper — declared here so all methods in this class can use it
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Creates: a server that accepts one client, echoes its messages, then exits
    public static void main(String[] args) throws IOException {
        if (DB_PASS == null || DB_PASS.isBlank()) {
            throw new IllegalStateException("Set TEST_DB_PASS before running ServerMain");
        }

        JdbcDepartmentDao departmentDao = new JdbcDepartmentDao(DB_URL, DB_USER, DB_PASS);

        System.out.println("Server listening on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            // accept() blocks until a client connects
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            try (PrintWriter  out = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8))) {

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Received: " + line);

                    // Parse the incoming request
                    ClientRequest request  = MAPPER.readValue(line, ClientRequest.class);
                    ServerResponse<?> response;

                    // Return all departments when no payload is needed.
                    if ("GET_ALL_DEPARTMENTS".equals(request.getType())) {
                        response = ServerResponse.success(
                                "Departments retrieved successfully",
                                departmentDao.getAllDepartments()
                        );

                        // Return one department by id, validating the payload before reading it.
                    } else if ("GET_DEPARTMENT_BY_ID".equals(request.getType())) {
                        JsonNode payload = request.getPayload();

                        if (payload == null || !payload.has("id")) {
                            response = ServerResponse.failure("Missing required field: id");
                    } else {
                            int id = payload.get("id").asInt();

                            response = departmentDao.getDepartmentById(id)
                                    .<ServerResponse<?>>map(department -> ServerResponse.success(
                                            "Department retrieved successfully",
                                            department
                                    ))
                                    .orElseGet(() -> ServerResponse.failure(
                                            "Department not found for id: " + id
                                    ));
                        }
                    } else {
                            response = ServerResponse.failure(
                                "Unsupported request type: " + request.getType()
                        );
                    }

                    // Send the response back on one line
                    out.println(MAPPER.writeValueAsString(response));
                }

                System.out.println("Client disconnected");
            }
        }
    }
}
