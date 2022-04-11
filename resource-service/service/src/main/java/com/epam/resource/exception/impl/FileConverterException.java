package com.epam.resource.exception.impl;

import com.epam.resource.exception.ResourceServiceException;

public class FileConverterException extends ResourceServiceException {
    public FileConverterException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
