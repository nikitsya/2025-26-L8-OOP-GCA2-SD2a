package com.supermarketstore.protocol;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A typed request envelope sent from client to server.
 * The 'type' field identifies the operation; 'payload' carries its parameters.
 *
 * @author Hanna Bokariuk
 */
public class ClientRequest {

    // === Fields ===
    private String   fType;
    private JsonNode fPayload;

    // === Constructors ===
    // Creates: empty request — required by Jackson
    public ClientRequest() {
        fType    = "";
        fPayload = null;
    }

    // Creates: request with a type and a JsonNode payload
    public ClientRequest(String type, JsonNode payload) {
        fType    = type;
        fPayload = payload;
    }

    // === Public API ===
    // Gets: the operation type constant
    public String getType() { return fType; }

    // Sets: the operation type constant
    public void setType(String type) { fType = type; }

    // Gets: the raw JSON payload node (may be null for no-parameter requests)
    public JsonNode getPayload() { return fPayload; }

    // Sets: the raw JSON payload node
    public void setPayload(JsonNode payload) { fPayload = payload; }
}