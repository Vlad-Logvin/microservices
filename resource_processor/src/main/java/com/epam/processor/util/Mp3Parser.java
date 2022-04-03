package com.epam.processor.util;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;

public class Mp3Parser {
    public static String getArtist(File file) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File mp3File = new Mp3File(file);
        return mp3File.getId3v1Tag().getArtist();
    }
    public static String getAlbum(File file) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File mp3File = new Mp3File(file);
        return mp3File.getId3v1Tag().getAlbum();
    }
    public static String getTitle(File file) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File mp3File = new Mp3File(file);
        return mp3File.getId3v1Tag().getTitle();
    }
    public static long getLengthInSeconds(File file) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File mp3File = new Mp3File(file);
        return mp3File.getLengthInSeconds();
    }
    public static String getYear(File file) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File mp3File = new Mp3File(file);
        return mp3File.getId3v1Tag().getYear();
    }
}
