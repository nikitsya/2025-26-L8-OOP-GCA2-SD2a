package com.supermarketstore.protocol;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A typed request envelope sent from client to server.
 * The type field identifies the operation, and the payload carries its parameters.
 *
 * @author Hanna Bokariuk
 * @author Nikita Smiichyk (contributor - documentation and refactoring)
 */
public class ClientRequest {
    // === Fields ===
    private String _type;
    private JsonNode _payload;

    // === Constructors ===

    /**
     * Creates an empty request required for JSON deserialisation.
     */
    public ClientRequest() {
        _type = "";
        _payload = null;
    }

    /**
     * Creates a request with an operation type and optional payload.
     *
     * @param type    the request type
     * @param payload the request payload, or null if no parameters are required
     */
    public ClientRequest(String type, JsonNode payload) {
        _type = type;
        _payload = payload;
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }

    public JsonNode getPayload() {
        return _payload;
    }

    public void setPayload(JsonNode payload) {
        _payload = payload;
    }
}
