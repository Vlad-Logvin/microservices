package com.epam.song_service.exception.handling;

import com.epam.song_service.exception.SongServiceException;
import com.epam.song_service.exception.impl.IdValidationException;
import com.epam.song_service.exception.impl.SongNotFoundException;
import com.epam.song_service.exception.impl.SongValidationException;
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
        return handleError(e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(SongNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse songNotFoundException(SongNotFoundException e) {
        logError(e);
        return handleError(e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(SongValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse songNotValidException(SongValidationException e) {
        logError(e);
        return handleError(e.getErrorCode(), e.getErrorMessage());
    }

    private ErrorResponse handleError(int errorCode, String errorMessage) {
        return new ErrorResponse(HttpStatus.valueOf(errorCode).toString(), errorMessage);
    }

    private void logError(SongServiceException e) {
        log.error("Error!", e);
    }
}
