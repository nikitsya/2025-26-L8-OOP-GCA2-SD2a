package com.supermarketstore.server;

import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.ServerResponse;

@FunctionalInterface
public interface RequestHandler {

    // Handles one client request and returns a response to send back to the client.
    ServerResponse<?> handle(ClientRequest request) throws Exception;
}
