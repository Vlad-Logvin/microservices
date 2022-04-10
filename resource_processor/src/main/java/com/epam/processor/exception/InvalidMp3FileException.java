package com.epam.processor.exception;

public class InvalidMp3FileException extends ResourceProcessorException{
    public InvalidMp3FileException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
