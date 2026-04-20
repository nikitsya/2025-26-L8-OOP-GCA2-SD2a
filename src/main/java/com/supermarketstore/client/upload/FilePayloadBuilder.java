package com.supermarketstore.client.upload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Builds reusable JSON payload fragments for attaching file data and metadata
 * to entity requests sent by the client.
 *
 * @author Nikita Smiichyk
 */
public class FilePayloadBuilder {
    // === Static Fields ===
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // === Methods ===

    public ObjectNode buildUploadPayload(Path filePath) throws IOException {
        ObjectNode filePayload = MAPPER.createObjectNode();
        ArrayNode fileDataNode = MAPPER.createArrayNode();

        byte[] bytes = Files.readAllBytes(filePath);
        for (byte b : bytes) {
            fileDataNode.add(b & 0xFF);
        }

        String fileName = filePath.getFileName().toString();
        String detectedMime = Files.probeContentType(filePath);
        String contentType = detectedMime != null ? detectedMime : "application/octet-stream";

        filePayload.set("fileData", fileDataNode);
        filePayload.put("fileName", fileName);
        filePayload.put("contentType", contentType);
        filePayload.put("fileSize", bytes.length);

        return filePayload;
    }
}
