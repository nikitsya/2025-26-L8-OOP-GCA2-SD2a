package com.supermarketstore.client.upload;

/**
 * DTO carrying a file upload payload, including Base64-encoded binary content
 * and associated metadata fields.
 *
 * @author OOP Teaching Team
 * @author Nikita Smiichyk (adapted for shared entity file upload handling)
 */
public class FileUploadPayload {

    // === Fields ===
    private int _entityId;
    private String _fileName;
    private String _contentType;
    private int _fileSize;
    private String _fileData;     // Base64-encoded binary content

    // === Constructors ===

    /**
     * Creates an empty file upload payload instance for Jackson deserialization.
     */
    public FileUploadPayload() {
    }

    /**
     * Creates a file upload payload with the target entity id, file metadata,
     * and Base64-encoded file content.
     *
     * @param entityId the identifier of the entity that will receive the file
     * @param fileName the original uploaded file name
     * @param contentType the MIME type of the uploaded file
     * @param fileSize the uploaded file size in bytes
     * @param fileData the Base64-encoded file content
     */
    public FileUploadPayload(int entityId, String fileName, String contentType, int fileSize, String fileData) {
        _entityId = entityId;
        _fileName = fileName;
        _contentType = contentType;
        _fileSize = fileSize;
        _fileData = fileData;
    }

    // === Public API ===

    public int getEntityId() {
        return _entityId;
    }

    public void setEntityId(int entityId) {
        _entityId = entityId;
    }

    public String getFileName() {
        return _fileName;
    }

    public void setFileName(String f) {
        _fileName = f;
    }

    public String getContentType() {
        return _contentType;
    }

    public void setContentType(String ct) {
        _contentType = ct;
    }

    public int getFileSize() {
        return _fileSize;
    }

    public void setFileSize(int fileSize) {
        _fileSize = fileSize;
    }

    public String getFileData() {
        return _fileData;
    }

    public void setFileData(String fileData) {
        _fileData = fileData;
    }
}
