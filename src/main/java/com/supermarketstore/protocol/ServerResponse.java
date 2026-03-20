package com.supermarketstore.protocol;

/**
 * A generic response wrapper that standardises all server replies.
 * Carries a status string, a human-readable message, and an optional data payload.
 *
 * @param <T> the type of the data payload (may be null on failure)
 */

public class ServerResponse<T> {

    // === Fields ===
    private String fStatus;
    private String fMessage;
    private T      fData;

    // === Constructors ===
    // Creates: empty response — required by Jackson for deserialisation
    public ServerResponse() {
        fStatus  = "";
        fMessage = "";
        fData    = null;
    }

    // Creates: response with all fields set
    public ServerResponse(String status, String message, T data) {
        fStatus  = status;
        fMessage = message;
        fData    = data;
    }

    // === Public API ===
    // Gets: the response status ("SUCCESS" or "FAILURE")
    public String getStatus() { return fStatus; }

    // Sets: the response status
    public void setStatus(String status) { fStatus = status; }

    // Gets: the human-readable result message
    public String getMessage() { return fMessage; }

    // Sets: the result message
    public void setMessage(String message) { fMessage = message; }

    // Gets: the response payload; null on failure responses
    public T getData() { return fData; }

    // Sets: the response payload
    public void setData(T data) { fData = data; }

    // === Helpers ===
    // Creates: a success response carrying the given data payload
    public static <T> ServerResponse<T> success(String message, T data) {
        return new ServerResponse<>("SUCCESS", message, data);
    }

    // Creates: a failure response with a null data payload
    public static <T> ServerResponse<T> failure(String message) {
        return new ServerResponse<>("FAILURE", message, null);
    }
}
