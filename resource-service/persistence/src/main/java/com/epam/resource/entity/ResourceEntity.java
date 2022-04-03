package com.epam.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "resource")
@Data
@NoArgsConstructor
public class ResourceEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "file_name")
    private String fileName;

    public ResourceEntity(String fileName) {
        this.fileName = fileName;
    }
}
