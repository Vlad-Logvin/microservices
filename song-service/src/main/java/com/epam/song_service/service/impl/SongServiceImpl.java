package com.epam.song_service.service.impl;

import com.epam.song_service.exception.impl.SongNotFoundException;
import com.epam.song_service.model.Song;
import com.epam.song_service.repository.SongRepository;
import com.epam.song_service.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    @Autowired
    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public Song save(Song song) {
        return songRepository.save(song);
    }

    @Override
    public Song findById(long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " was not found"));
    }

    @Override
    public List<Long> deleteByIds(List<Long> ids) {
        return songRepository.deleteByIdIn(ids)
                .stream()
                .map(Song::getId)
                .collect(Collectors.toList());
    }
}
