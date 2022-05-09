package com.epam.storage.service.exception;

import lombok.Data;

@Data
public class StorageServiceException extends RuntimeException {
    private Throwable cause;
    private String errorMessage;
    private int errorCode;

    public StorageServiceException(Throwable cause, String errorMessage, int errorCode) {
        this.cause = cause;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public StorageServiceException(String errorMessage, int errorCode) {
        this(null, errorMessage, errorCode);
    }

    public StorageServiceException() {
        super();
    }
}
