package com.epam.song_service.exception.impl;

import com.epam.song_service.exception.SongServiceException;

public class IdValidationException extends SongServiceException {
    public IdValidationException(String message) {
        super(message);
    }
}
