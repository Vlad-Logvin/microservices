package com.epam.processor.exception.impl;

import com.epam.processor.exception.ResourceProcessorException;

public class QueryProcessingException extends ResourceProcessorException {
    public QueryProcessingException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
