package com.epam.song_service.exception.impl;

import com.epam.song_service.exception.SongServiceException;

public class SongNotFoundException extends SongServiceException {
    public SongNotFoundException(String message) {
        super(message);
    }
}
