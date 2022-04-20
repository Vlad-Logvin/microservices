package com.epam.processor.exception.impl;

import com.epam.processor.exception.ResourceProcessorException;

public class InvalidMp3FileException extends ResourceProcessorException {
    public InvalidMp3FileException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
