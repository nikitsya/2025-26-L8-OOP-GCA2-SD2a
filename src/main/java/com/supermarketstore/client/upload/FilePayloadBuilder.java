package com.supermarketstore.client.upload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * Builds reusable JSON payload fragments containing Base64-encoded file data
 * and related metadata for entity upload requests sent by the client.
 *
 * @author Nikita Smiichyk
 */
public class FilePayloadBuilder {
    // === Static Fields ===
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // === Methods ===

    /**
     * Builds a JSON payload fragment containing Base64-encoded file content
     * and related metadata for an entity upload request.
     *
     * @param filePath the path to the file that will be attached to the request
     * @return an object node containing fileData, fileName, contentType, and fileSize
     * @throws IOException if the file cannot be read or its content type cannot be determined
     */
    public ObjectNode buildUploadPayload(Path filePath) throws IOException {
        ObjectNode filePayload = MAPPER.createObjectNode();

        byte[] bytes = Files.readAllBytes(filePath);
        String fileData = Base64.getEncoder().encodeToString(bytes);
        String fileName = filePath.getFileName().toString();
        String detectedMime = Files.probeContentType(filePath);
        String contentType = detectedMime != null ? detectedMime : "application/octet-stream";

        filePayload.put("fileData", fileData);
        filePayload.put("fileName", fileName);
        filePayload.put("contentType", contentType);
        filePayload.put("fileSize", bytes.length);

        return filePayload;
    }
}
