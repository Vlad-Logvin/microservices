package com.epam.song_service.repository;

import com.epam.song_service.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> deleteByIdIn(List<Long> ids);
}
