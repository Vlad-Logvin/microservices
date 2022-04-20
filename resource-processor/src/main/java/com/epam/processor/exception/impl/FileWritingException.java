package com.epam.processor.exception.impl;

import com.epam.processor.exception.ResourceProcessorException;

public class FileWritingException extends ResourceProcessorException {
    public FileWritingException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
