package com.epam.processor.exception;

public class QueryProcessingException extends ResourceProcessorException {
    public QueryProcessingException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
