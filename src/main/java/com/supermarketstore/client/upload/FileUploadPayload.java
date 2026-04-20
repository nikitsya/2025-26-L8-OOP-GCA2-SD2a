package com.supermarketstore.client.upload;

/**
 * DTO carrying a file upload payload, including Base64-encoded binary content
 * and associated metadata fields.
 *
 * @author OOP Teaching Team
 */
public class FileUploadPayload {

    // === Fields ===
    private int _entityId;
    private String _fileName;
    private String _contentType;
    private int _fileSize;
    private String _fileData;     // Base64-encoded binary content

    // === Constructors ===
    // Creates: empty payload — required by Jackson
    public FileUploadPayload() {
    }

    // Creates: fully populated upload payload
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
