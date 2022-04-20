package com.epam.processor.exception.impl;

import com.epam.processor.exception.ResourceProcessorException;

public class SongServiceException extends ResourceProcessorException {
    public SongServiceException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }

    public SongServiceException(String errorMessage, int errorCode) {
        super(errorMessage, errorCode);
    }
}
