package com.supermarketstore.client.upload;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for {@link FilePayloadBuilder}.
 */
class FilePayloadBuilderTest {

    @TempDir
    private Path tempDirectory;

    @Test
    void buildUploadPayload_readsFileMetadataAndBase64Content() throws Exception {
        Path uploadFile = tempDirectory.resolve("upload.bin");
        byte[] content = "client upload content".getBytes(StandardCharsets.UTF_8);
        Files.write(uploadFile, content);

        ObjectNode payload = new FilePayloadBuilder().buildUploadPayload(uploadFile);

        assertEquals(Base64.getEncoder().encodeToString(content), payload.get("fileData").asText());
        assertEquals("upload.bin", payload.get("fileName").asText());
        assertEquals(content.length, payload.get("fileSize").asInt());
        assertFalse(payload.get("contentType").asText().isBlank());
    }
}
