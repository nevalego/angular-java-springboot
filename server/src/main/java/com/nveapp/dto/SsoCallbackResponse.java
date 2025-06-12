package com.nveapp.dto;

public class SsoCallbackResponse {
    private String status; // "success" or "error"
    private String message;
    private String token; // JWT token if successful

    public SsoCallbackResponse(String status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

    public SsoCallbackResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.token = null;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}