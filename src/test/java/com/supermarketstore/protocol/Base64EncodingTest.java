package com.supermarketstore.protocol;

import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Unit tests for Base64 encode/decode round-trips used by the project file flows.
 */
class Base64EncodingTest {

    @Test
    void base64EncodeDecodeRoundTrip_whenPayloadIsSmall_preservesBytes() {
        byte[] original = {72, 101, 108, 108, 111};

        String encoded = Base64.getEncoder().encodeToString(original);
        byte[] decoded = Base64.getDecoder().decode(encoded);

        assertArrayEquals(original, decoded);
    }

    @Test
    void base64EncodeDecodeRoundTrip_whenPayloadIsLarger_preservesBytes() {
        byte[] original = new byte[1024];
        new Random(42).nextBytes(original);

        String encoded = Base64.getEncoder().encodeToString(original);
        byte[] decoded = Base64.getDecoder().decode(encoded);

        assertArrayEquals(original, decoded);
    }
}
