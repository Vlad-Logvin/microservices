package com.epam.resource.exception.impl;

import com.epam.resource.exception.ResourceServiceException;

public class AmazonException extends ResourceServiceException {
    public AmazonException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
