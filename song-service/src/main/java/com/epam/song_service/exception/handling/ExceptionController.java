package com.epam.song_service.exception.handling;

import com.epam.song_service.exception.impl.IdValidationException;
import com.epam.song_service.exception.impl.SongNotFoundException;
import com.epam.song_service.exception.impl.SongValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IdValidationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse idValidationException(IdValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(SongNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse songNotFoundException(SongNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(SongValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse songNotValidException(SongValidationException e) {
        return new ErrorResponse(e.getMessage());
    }
}
