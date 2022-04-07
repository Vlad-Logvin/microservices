package com.epam.processor.exception;

public class ResourceProcessorException extends RuntimeException {
    private String errorMessage;
    private String errorCode;

    public ResourceProcessorException(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
