package com.tejko.yamb.api.dto.responses;

import java.time.Instant;

import org.springframework.hateoas.RepresentationModel;

public class ErrorResponse extends RepresentationModel<ErrorResponse> {

    private int status;
    private String error;
    private String message;
    private Instant timestamp;
    private String path;
    
    public ErrorResponse() {}
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}