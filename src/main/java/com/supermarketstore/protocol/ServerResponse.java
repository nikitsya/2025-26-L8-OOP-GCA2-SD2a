package com.supermarketstore.protocol;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a standard JSON response sent from the server to a client.
 *
 * @param <T> the type of the response data payload
 */
public class ServerResponse<T> {
    // === Fields ===
    private String _status;
    private String _message;
    private T _data;

    // === Constructors ===

    /**
     * Creates an empty response.
     * Required for Jackson deserialisation.
     */
    public ServerResponse() {
    }

    /**
     * Creates a response with a status, message, and data payload.
     *
     * @param status  Typically "OK" or "ERROR".
     * @param message Human-readable response message.
     * @param data    Optional payload data.
     */
    public ServerResponse(String status, String message, T data) {
        if (status == null || status.isBlank()) throw new IllegalArgumentException("status is required");
        _status = status;
        _message = message;
        _data = data;
    }

    // === Methods ===

    /**
     * Creates a successful response.
     *
     * @param message the response message
     * @param data    the optional response payload
     * @param <T>     the response payload type
     * @return a response with OK status
     */
    public static <T> ServerResponse<T> ok(String message, T data) {
        return new ServerResponse<>("OK", message, data);
    }

    /**
     * Creates an error response without a data payload.
     *
     * @param message the response message
     * @param <T>     the response payload type
     * @return a response with ERROR status
     */
    public static <T> ServerResponse<T> error(String message) {
        return new ServerResponse<>("ERROR", message, null);
    }

    // === Properties ===

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public T getData() {
        return _data;
    }

    public void setData(T data) {
        _data = data;
    }

    /**
     * Convenience helper for Java code only.
     * It should not appear in JSON.
     *
     * @return true when the status is OK
     */
    @JsonIgnore
    public boolean isOk() {
        return "OK".equals(_status);
    }
}
