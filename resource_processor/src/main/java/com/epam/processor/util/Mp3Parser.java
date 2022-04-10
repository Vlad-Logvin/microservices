package com.epam.processor.util;

import com.epam.processor.dto.SongDTO;
import com.epam.processor.exception.InvalidMp3FileException;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public record Mp3Parser() {

    @Autowired
    public Mp3Parser {
    }

    public SongDTO parseFile(File file) {
        Mp3File mp3File = createMp3File(file);
        return new SongDTO(parseName(mp3File), parseArtist(mp3File), parseAlbum(mp3File), parseLength(mp3File), parseYear(mp3File));
    }

    private Mp3File createMp3File(File file) {
        try {
            return new Mp3File(file);
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            throw new InvalidMp3FileException("MP3 file doesn't valid", "400");
        }
    }

    private String parseAlbum(Mp3File mp3File) {
        return mp3File.getId3v1Tag().getAlbum();
    }

    private String parseName(Mp3File mp3File) {
        return mp3File.getId3v1Tag().getTitle();
    }

    private String parseArtist(Mp3File mp3File) {
        return mp3File.getId3v1Tag().getArtist();
    }

    private String parseYear(Mp3File mp3File) {
        return mp3File.getId3v1Tag().getYear();
    }

    private String parseLength(Mp3File mp3File) {
        return mp3File.getLengthInSeconds() / 60 + ":" + mp3File.getLengthInSeconds() % 60;
    }
}
