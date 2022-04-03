package com.epam.resource.service;

import com.epam.resource.dto.ResourceDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResourceService {
    byte[] findById(long id);

    ResourceDTO save(MultipartFile mp3File);

    List<Long> delete(List<Long> ids);
}
