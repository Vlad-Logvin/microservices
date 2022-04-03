package com.epam.song_service.exception;

public class SongServiceException extends RuntimeException {
    public SongServiceException(String message) {
        super(message);
    }
}
