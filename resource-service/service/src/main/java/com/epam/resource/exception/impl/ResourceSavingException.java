package com.epam.resource.exception.impl;

import com.epam.resource.exception.ResourceServiceException;

public class ResourceSavingException extends ResourceServiceException {
    public ResourceSavingException(String message) {
        super(message);
    }
}
