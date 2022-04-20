package com.epam.resource.exception;

import lombok.Data;

@Data
public class ResourceServiceException extends RuntimeException {
    private Throwable cause;
    private String errorMessage;
    private int errorCode;

    public ResourceServiceException(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ResourceServiceException(Throwable cause, String errorMessage, int errorCode) {
        this.cause = cause;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ResourceServiceException() {
        super();
    }
}
