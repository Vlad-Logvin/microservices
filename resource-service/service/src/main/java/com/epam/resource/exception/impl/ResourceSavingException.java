package com.epam.resource.exception.impl;

import com.epam.resource.exception.ResourceServiceException;

public class ResourceSavingException extends ResourceServiceException {
    public ResourceSavingException(String errorMessage, int errorCode) {
        super(errorMessage, errorCode);
    }

    public ResourceSavingException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
