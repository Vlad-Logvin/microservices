package com.epam.processor.exception.impl;

import com.epam.processor.exception.ResourceProcessorException;

public class RestException extends ResourceProcessorException {
    public RestException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
