package com.epam.processor.exception.impl;

import com.epam.processor.exception.ResourceProcessorException;

public class ResourceServiceException extends ResourceProcessorException {
    public ResourceServiceException(String errorMessage, int errorCode) {
        super(errorMessage, errorCode);
    }

    public ResourceServiceException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
