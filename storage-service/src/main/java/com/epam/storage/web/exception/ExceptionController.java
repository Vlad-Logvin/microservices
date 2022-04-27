package com.epam.storage.web.exception;

import com.epam.storage.model.StorageType;
import com.epam.storage.service.exception.storage.*;
import com.epam.storage.service.exception.validation.IdValidationException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(IdValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse idValidationException(IdValidationException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(StorageNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse storageNotFoundException(StorageNotFoundException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(StorageSavingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse storageSavingException(StorageSavingException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(StorageFindingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse storageFindingException(StorageFindingException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(StorageDeletingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse storageDeletingException(StorageDeletingException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(StorageValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse storageValidationException(StorageValidationException e) {
        logError(e);
        return handleError(HttpStatus.valueOf(e.getErrorCode()), e.getErrorMessage());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ErrorResponse invalidFormatException(InvalidFormatException e) {
        if (e.getTargetType().isAssignableFrom(StorageType.class)) {
            logError(e);
            return handleError(HttpStatus.valueOf(400),
                    "Storage type value should be in " + Arrays.stream(StorageType.values())
                            .map(Enum::toString).collect(Collectors.joining(", ")));
        } else {
            logError(e);
            return handleError(HttpStatus.valueOf(500), e.getMessage());
        }
    }

    private ErrorResponse handleError(HttpStatus httpStatus, String errorMessage) {
        return new ErrorResponse(httpStatus.toString(), errorMessage);
    }

    private void logError(Throwable cause) {
        log.error("Error!", cause);
    }
}
