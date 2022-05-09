package com.epam.storage.service.exception.storage;

import com.epam.storage.service.exception.StorageServiceException;

public class StorageNotFoundException extends StorageServiceException {
    public StorageNotFoundException(String errorMessage, int errorCode) {
        super(errorMessage, errorCode);
    }
}
