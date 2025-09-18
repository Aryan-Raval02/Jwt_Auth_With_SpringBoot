package com.task.JwtAuthentication.Exception;

public class CustomErrorResponse
{
    private int status;
    private String message;
    private long Timestamp;

    public CustomErrorResponse(){}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }
}
