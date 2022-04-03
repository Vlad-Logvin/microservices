package com.epam.song_service.service;

import com.epam.song_service.model.Song;

import java.util.List;

public interface SongService {
    Song save(Song song);
    Song findById(long id);
    List<Long> deleteByIds(List<Long> ids);
}
