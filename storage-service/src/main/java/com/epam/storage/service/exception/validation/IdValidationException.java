package com.epam.storage.service.exception.validation;

import com.epam.storage.service.exception.StorageServiceException;

public class IdValidationException extends StorageServiceException {
    public IdValidationException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }

    public IdValidationException(String errorMessage, int errorCode) {
        super(errorMessage, errorCode);
    }

    public IdValidationException() {
        super();
    }
}
