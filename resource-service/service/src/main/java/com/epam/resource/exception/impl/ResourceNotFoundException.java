package com.epam.resource.exception.impl;

import com.epam.resource.exception.ResourceServiceException;

public class ResourceNotFoundException extends ResourceServiceException {
    public ResourceNotFoundException(String errorMessage, int errorCode) {
        super(errorMessage, errorCode);
    }
}
