package com.supermarketstore.server.routing;

import com.supermarketstore.protocol.ClientRequest;
import com.supermarketstore.protocol.ServerResponse;

/**
 * Handles a single client request and returns the server response that should be sent back.
 */
@FunctionalInterface
public interface RequestHandler {

    /**
     * Handles one client request.
     *
     * @param request the client request to process
     * @return the response to send back to the client
     * @throws Exception if request processing fails
     */
    ServerResponse<?> handle(ClientRequest request) throws Exception;
}
