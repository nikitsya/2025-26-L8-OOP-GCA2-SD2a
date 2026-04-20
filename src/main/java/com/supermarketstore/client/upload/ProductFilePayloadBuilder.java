package com.supermarketstore.client.upload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProductFilePayloadBuilder {
    private static final ObjectMapper MAPPER = new ObjectMapper();

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

        filePayload.put("fileData", fileDataNode);
        filePayload.put("fileName", fileName);
        filePayload.put("contentType", contentType);
        filePayload.put("fileSize", bytes.length);

        return filePayload;
    }
}
