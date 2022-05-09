package com.epam.resource.exception.impl;

import com.epam.resource.exception.ResourceServiceException;

public class StorageServiceException extends ResourceServiceException {
    public StorageServiceException(String errorMessage, int errorCode) {
        super(errorMessage, errorCode);
    }
}
