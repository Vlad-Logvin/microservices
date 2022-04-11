package com.epam.resource.exception.impl;

import com.epam.resource.exception.ResourceServiceException;

public class RabbitException extends ResourceServiceException {
    public RabbitException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
