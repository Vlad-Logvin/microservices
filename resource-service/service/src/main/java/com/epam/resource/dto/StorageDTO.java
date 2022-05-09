package com.epam.resource.dto;

import lombok.Data;

@Data
public class StorageDTO {
    private long id;
    private String bucket;
    private String path;
    private StorageType storageType;

    public enum StorageType {
        STAGING, PERMANENT
    }
}
