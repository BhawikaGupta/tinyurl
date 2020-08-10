package com.tiny.url.model;

public class ErrorResponse {
    String status;

    String errorCode;

    public ErrorResponse(String status, String errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
