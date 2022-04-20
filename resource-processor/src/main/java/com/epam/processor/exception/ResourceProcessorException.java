package com.epam.processor.exception;

import lombok.Data;

@Data
public class ResourceProcessorException extends RuntimeException {
    private final Throwable cause;
    private final String errorMessage;
    private final int errorCode;

    public ResourceProcessorException(Throwable cause, String errorMessage, int errorCode) {
        this.cause = cause;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ResourceProcessorException(String errorMessage, int errorCode) {
        this(null, errorMessage, errorCode);
    }
}
