package com.epam.resource.exception.impl;

public class AmazonException extends ResourceSavingException {
    public AmazonException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }
}
