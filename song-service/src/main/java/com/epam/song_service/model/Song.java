package com.epam.song_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Entity(name = "songs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Song implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    @NotNull(message = "name must not be null")
    private String name;

    @Column(name = "artist")
    @NotNull(message = "artist must not be null")
    private String artist;

    @Column(name = "album")
    @NotNull(message = "album must not be null")
    private String album;

    @Column(name = "length")
    @NotNull(message = "length must not be null")
    @Length(max = 6, message = "Max length of length field equals 6")
    private String length;

    @Column(name = "resource_id")
    @NotNull(message = "resourceId must not be null")
    private Long resourceId;

    @Column(name = "year")
    @NotNull(message = "year must not be null")
    private String year;
}
