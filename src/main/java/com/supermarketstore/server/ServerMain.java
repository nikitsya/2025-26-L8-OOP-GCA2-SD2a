package com.supermarketstore.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Request;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.ServerResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerMain {
    private static final int PORT = 9000;

    // Creates: a single shared mapper — declared here so all methods in this class can use it
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Creates: a server that accepts one client, echoes its messages, then exits
    public static void main(String[] args) throws IOException {
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
                    ServerResponse<String> response = ServerResponse.success(
                            "Echo: " + request.getType(), "ok"
                    );
                    // Send the response back on one line
                    out.println(MAPPER.writeValueAsString(response));
                }

                System.out.println("Client disconnected");
            }
        }
    }
}
