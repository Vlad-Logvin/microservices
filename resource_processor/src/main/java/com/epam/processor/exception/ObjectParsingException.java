package com.epam.processor.exception;

public class ObjectParsingException extends ResourceProcessorException {
    public ObjectParsingException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
