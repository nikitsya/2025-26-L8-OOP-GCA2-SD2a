package com.supermarketstore.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ServerResponse}.
 */
class ServerResponseTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void ok_setsStatusMessageDataAndIsOk() {
        ServerResponse<String> response = ServerResponse.ok("done", "hello");

        assertEquals("OK", response.getStatus());
        assertEquals("done", response.getMessage());
        assertEquals("hello", response.getData());
        assertTrue(response.isOk());
    }

    @Test
    void error_setsStatusMessageNullDataAndIsNotOk() {
        ServerResponse<String> response = ServerResponse.error("not found");

        assertEquals("ERROR", response.getStatus());
        assertEquals("not found", response.getMessage());
        assertNull(response.getData());
        assertFalse(response.isOk());
    }

    @Test
    void ok_whenSerialisedToJson_containsStatusAndPayload() throws Exception {
        ServerResponse<Integer> response = ServerResponse.ok("created", 42);

        String json = mapper.writeValueAsString(response);

        assertTrue(json.contains("\"status\":\"OK\""));
        assertTrue(json.contains("\"message\":\"created\""));
        assertTrue(json.contains("\"data\":42"));
    }
}
