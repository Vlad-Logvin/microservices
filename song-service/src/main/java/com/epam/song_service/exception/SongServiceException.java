package com.epam.song_service.exception;

import lombok.Data;

@Data
public class SongServiceException extends RuntimeException {
    private Throwable cause;
    private String errorMessage;
    private int errorCode;

    public SongServiceException(Throwable cause, String errorMessage, int errorCode) {
        super(errorMessage, cause);
        this.cause = cause;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public SongServiceException(String errorMessage, int errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public SongServiceException() {
    }
}
