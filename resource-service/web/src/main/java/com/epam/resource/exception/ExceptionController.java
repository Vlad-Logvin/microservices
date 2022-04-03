package com.epam.resource.exception;

import com.epam.resource.exception.impl.IdValidationException;
import com.epam.resource.exception.impl.ResourceNotFoundException;
import com.epam.resource.exception.impl.ResourceNotValidException;
import com.epam.resource.exception.impl.ResourceSavingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IdValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse idValidationException(IdValidationException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotFoundException(ResourceNotFoundException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), e.getMessage());
    }

    @ExceptionHandler(ResourceNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse resourceNotValidException(ResourceNotValidException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
    }

    @ExceptionHandler(ResourceSavingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse resourceSavingException(ResourceSavingException e) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
    }
}
