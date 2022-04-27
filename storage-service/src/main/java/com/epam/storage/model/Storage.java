package com.epam.storage.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "storages", uniqueConstraints = {@UniqueConstraint(name = "UNQ_bucket", columnNames = "bucket")})
@Data
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "bucket")
    @NotNull(message = "Bucket name can't be null")
    @Length(min = 2, max = 64, message = "The bucket name must be of limited length from 2 to 64")
    private String bucket;

    @Column(name = "staging_type")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Storage type can't be null")
    private StorageType storageType;

    @Column(name = "path")
    @Length(max = 256, message = "Path can't be more than 256 symbols")
    private String path;
}
