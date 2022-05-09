package com.epam.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDTO {
    private long id;
    private String fileName;
    private long storageId;

    public ResourceDTO(String fileName) {
        this.fileName = fileName;
    }
}
