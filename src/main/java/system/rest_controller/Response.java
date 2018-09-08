package system.rest_controller;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public class Response {
    private int status;
    private String error;
    private String timestamp;
    private String message;

    public Response(HttpStatus httpStatus,String errorMessage){
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = errorMessage;
        this.timestamp = Instant.now().toString();
    }

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
