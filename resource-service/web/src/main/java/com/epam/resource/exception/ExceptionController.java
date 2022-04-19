package com.epam.resource.exception;

import com.epam.resource.exception.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(IdValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse idValidationException(IdValidationException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotFoundException(ResourceNotFoundException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(ResourceNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse resourceNotValidException(ResourceNotValidException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(ResourceSavingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse resourceSavingException(ResourceSavingException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(AmazonException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse amazonException(AmazonException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(FileConverterException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse fileConverterException(FileConverterException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(RabbitException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse rabbitException(RabbitException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    private ErrorResponse handleError(HttpStatus httpStatus, String errorMessage) {
        return new ErrorResponse(httpStatus.toString(), errorMessage);
    }

    private void logError(Throwable cause) {
        log.error("Error!", cause);
    }
}
