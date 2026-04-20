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
    // Gets: the entity id this file is associated with
    public int getEntityId() {
        return _entityId;
    }

    // Sets: the entity id
    public void setEntityId(int entityId) {
        _entityId = entityId;
    }

    // Gets: the original filename including extension
    public String getFileName() {
        return _fileName;
    }

    // Sets: the original filename
    public void setFileName(String f) {
        _fileName = f;
    }

    // Gets: the MIME content type (e.g. "image/png")
    public String getContentType() {
        return _contentType;
    }

    // Sets: the MIME content type
    public void setContentType(String ct) {
        _contentType = ct;
    }

    // Gets: the file size in bytes (pre-encoding)
    public int getFileSize() {
        return _fileSize;
    }

    // Sets: the file size in bytes
    public void setFileSize(int fileSize) {
        _fileSize = fileSize;
    }

    // Gets: the Base64-encoded file content
    public String getFileData() {
        return _fileData;
    }

    // Sets: the Base64-encoded file content
    public void setFileData(String fileData) {
        _fileData = fileData;
    }
}
