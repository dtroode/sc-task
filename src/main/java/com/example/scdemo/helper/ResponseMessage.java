package com.example.scdemo.helper;

// Entity made for returning messages from server to client.
public class ResponseMessage {
    // Contains only message field and methods to access it.
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
