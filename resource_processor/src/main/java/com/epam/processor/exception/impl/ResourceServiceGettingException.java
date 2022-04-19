package com.epam.processor.exception.impl;

import com.epam.processor.exception.ResourceProcessorException;

public class ResourceServiceGettingException extends ResourceProcessorException {
    public ResourceServiceGettingException(String errorMessage, int errorCode) {
        super(errorMessage, errorCode);
    }
}
