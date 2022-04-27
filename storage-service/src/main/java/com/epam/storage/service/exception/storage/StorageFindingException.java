package com.epam.storage.service.exception.storage;

import com.epam.storage.service.exception.StorageServiceException;

public class StorageFindingException extends StorageServiceException {
    public StorageFindingException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
