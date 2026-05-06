package com.supermarketstore.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link ClientRequest}.
 */
class ClientRequestTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void jsonRoundTrip_preservesTypeAndPayload() throws Exception {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("name", "Bakery");
        payload.put("floor", 1);

        ClientRequest original = new ClientRequest("ADD_DEPARTMENT", payload);

        String json = mapper.writeValueAsString(original);
        ClientRequest deserialised = mapper.readValue(json, ClientRequest.class);

        assertEquals("ADD_DEPARTMENT", deserialised.getType());
        assertNotNull(deserialised.getPayload());
        assertEquals("Bakery", deserialised.getPayload().get("name").asText());
        assertEquals(1, deserialised.getPayload().get("floor").asInt());
    }
}
