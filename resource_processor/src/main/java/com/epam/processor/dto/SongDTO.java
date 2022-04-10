package com.epam.processor.dto;

import lombok.Data;

@Data
public class SongDTO {
    private String name;
    private String artist;
    private String album;
    private String length;
    private String year;
    private long resourceId;

    public SongDTO(String name, String artist, String album, String length, String year) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.length = length;
        this.year = year;
    }
}
