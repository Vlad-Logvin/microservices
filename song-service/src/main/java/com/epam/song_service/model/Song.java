package com.epam.song_service.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Entity(name = "songs")
@Data
public class Song implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "artist")
    @NotNull
    private String artist;

    @Column(name = "album")
    @NotNull
    private String album;

    @Column(name = "length")
    @NotNull
    @Length(max = 6)
    private String length;

    @Column(name = "resource_id")
    @NotNull
    private Long resourceId;

    @Column(name = "year")
    @NotNull
    private Integer year;
}
