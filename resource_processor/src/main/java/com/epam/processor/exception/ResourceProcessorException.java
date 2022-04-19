package com.epam.processor.exception;

public class ResourceProcessorException extends RuntimeException {
    private Throwable cause;
    private String errorMessage;
    private int errorCode;

    public ResourceProcessorException(Throwable cause, String errorMessage, int errorCode) {
        this.cause = cause;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ResourceProcessorException(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
