package com.epam.processor.exception.impl;

import com.epam.processor.exception.ResourceProcessorException;

public class ObjectParsingException extends ResourceProcessorException {
    public ObjectParsingException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
