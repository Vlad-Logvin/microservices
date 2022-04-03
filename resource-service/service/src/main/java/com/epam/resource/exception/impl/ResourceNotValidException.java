package com.epam.resource.exception.impl;

import com.epam.resource.exception.ResourceServiceException;

public class ResourceNotValidException extends ResourceServiceException {
    public ResourceNotValidException(String message) {
        super(message);
    }
}
