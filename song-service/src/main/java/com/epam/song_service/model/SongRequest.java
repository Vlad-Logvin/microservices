package com.epam.song_service.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
public class SongRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    private String artist;

    @NotNull
    private String album;

    @NotNull
    @Length(max = 6)
    private String length;

    @NotNull
    private Long resourceId;

    @NotNull
    private Integer year;
}
