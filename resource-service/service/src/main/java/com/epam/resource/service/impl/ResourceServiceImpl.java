package com.epam.resource.service.impl;

import com.epam.resource.dto.ResourceDTO;
import com.epam.resource.entity.ResourceEntity;
import com.epam.resource.exception.impl.ResourceNotFoundException;
import com.epam.resource.exception.impl.ResourceSavingException;
import com.epam.resource.repository.ResourceRepository;
import com.epam.resource.service.AmazonS3Service;
import com.epam.resource.service.ResourceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public record ResourceServiceImpl(ResourceRepository resourceRepository,
                                  AmazonS3Service amazonS3Service,
                                  ModelMapper modelMapper) implements ResourceService {

    @Autowired
    public ResourceServiceImpl {
    }

    @Override
    public byte[] findById(long id) {
        Optional<ResourceEntity> resource = resourceRepository.findById(id);
        if (resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource with id " + id + " was not found");
        }
        byte[] file = amazonS3Service.getObject("song-resource-service", resource.get().getFileName());
        if (file == null) {
            throw new ResourceNotFoundException("Error during loading from AWS");
        }
        return file;
    }

    @Override
    public ResourceDTO save(MultipartFile mp3File) {
        String fileName = System.currentTimeMillis() + "_" + mp3File.getOriginalFilename();
        boolean isSavedInAmazon = amazonS3Service.putObject("song-resource-service", fileName, mp3File);
        if (!isSavedInAmazon) {
            throw new ResourceSavingException("Error during saving to amazon");
        }
        ResourceEntity save = resourceRepository.save(new ResourceEntity(fileName));
        return entityToDto(save);
    }

    @Override
    public List<Long> delete(List<Long> ids) {
        List<Long> deletedIds = new ArrayList<>();
        ids.forEach(id -> {
            Optional<ResourceEntity> resourceEntity = resourceRepository.findById(id);
            if (resourceEntity.isEmpty()) {
                return;
            }
            ResourceEntity resource = resourceEntity.get();
            boolean isDeletedInAmazon = amazonS3Service.deleteObject("song-resource-service", resource.getFileName());
            if (isDeletedInAmazon) {
                resourceRepository.delete(resource);
                deletedIds.add(resource.getId());
            }
        });
        return deletedIds;
    }

    private ResourceDTO entityToDto(ResourceEntity resourceEntity) {
        return modelMapper.map(resourceEntity, ResourceDTO.class);
    }

    private ResourceEntity dtoToEntity(ResourceDTO resourceDTO) {
        return modelMapper.map(resourceDTO, ResourceEntity.class);
    }
}
