package com.epam.resource.exception.impl;

import com.epam.resource.exception.ResourceServiceException;

public class IdValidationException extends ResourceServiceException {
    public IdValidationException(String message) {
        super(message);
    }
}
