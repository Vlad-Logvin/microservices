package com.epam.storage.service.exception.storage;

import com.epam.storage.service.exception.StorageServiceException;

public class StorageDeletingException extends StorageServiceException {
    public StorageDeletingException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
