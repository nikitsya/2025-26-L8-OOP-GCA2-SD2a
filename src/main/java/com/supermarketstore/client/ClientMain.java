package com.supermarketstore.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.ServerResponse;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ClientMain {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Creates: a client that connects, sends one request, reads one response, then exits
    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket("localhost", 9000);
             PrintWriter  out = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            // Build and send a request
            //ClientRequest request = new ClientRequest("GET_ALL", null);
            ClientRequest request = new ClientRequest("GET_ALL_DEPARTMENTS", null);

            out.println(MAPPER.writeValueAsString(request));

            // Read and parse the response
            String             line     = in.readLine();
//            ServerResponse<String>   response = MAPPER.readValue(
//                    line, new TypeReference<ServerResponse<String>>() {}
//            );
            // Temporarily print the raw JSON response so we can verify the server is returning departments.
            System.out.println("Raw response: " + line);

//            System.out.println("Status:  " + response.getStatus());
//            System.out.println("Message: " + response.getMessage());
        }
    }
}