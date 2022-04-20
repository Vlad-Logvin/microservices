package com.epam.song_service.exception.impl;

import com.epam.song_service.exception.SongServiceException;

public class SongNotFoundException extends SongServiceException {
    public SongNotFoundException(Throwable cause, String errorMessage, int errorCode) {
        super(cause, errorMessage, errorCode);
    }

    public SongNotFoundException(String errorMessage, int errorCode) {
        super(errorMessage, errorCode);
    }
}
