package com.epam.processor.publisher;

import com.epam.processor.dto.SongDTO;

public interface SongServicePublisher {
    void sendSong(SongDTO songDTO);
}
