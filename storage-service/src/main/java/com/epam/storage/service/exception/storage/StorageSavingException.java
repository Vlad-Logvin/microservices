package com.epam.storage.service.exception.storage;

import com.epam.storage.service.exception.StorageServiceException;

public class StorageSavingException extends StorageServiceException {
    public StorageSavingException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
