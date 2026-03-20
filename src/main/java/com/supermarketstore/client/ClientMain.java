package com.supermarketstore.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.ServerResponse;
import com.supermarketstore.department.Department;


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ClientMain {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Creates: a client that connects, sends one request, reads one response, then exits
    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket("localhost", 9000);
             PrintWriter out = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            // Build and send a request
            //ClientRequest request = new ClientRequest("GET_ALL", null);
            ClientRequest request = new ClientRequest("GET_ALL_DEPARTMENTS", null);

            out.println(MAPPER.writeValueAsString(request));

            // Read and parse the response
            String line = in.readLine();
            ServerResponse<List<Department>> response = MAPPER.readValue(
                    line, new TypeReference<ServerResponse<List<Department>>>() {
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
        }
    }
}