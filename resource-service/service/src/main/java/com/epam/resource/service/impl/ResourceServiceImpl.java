package com.epam.resource.service.impl;

import com.epam.resource.dto.ResourceDTO;
import com.epam.resource.dto.StorageDTO;
import com.epam.resource.entity.ResourceEntity;
import com.epam.resource.exception.impl.ResourceNotFoundException;
import com.epam.resource.exception.impl.ResourceSavingException;
import com.epam.resource.repository.ResourceRepository;
import com.epam.resource.service.AmazonS3Service;
import com.epam.resource.service.ResourceService;
import com.epam.resource.service.StorageService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final AmazonS3Service amazonS3Service;
    private final ModelMapper modelMapper;
    private final StorageService storageService;
    private final StorageService.StorageTypeFilter storageTypeFilter;

    @Override
    public byte[] findById(long id) {
        ResourceEntity resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with id " + id + " was not found", 400));
        return processGetting(resource);
    }

    @Override
    public ResourceDTO save(MultipartFile mp3File) {
        String fileName = getFileName(mp3File);
        StorageDTO stagingStorage = storageTypeFilter.findStagingStorage();
        processSavingToAmazonS3(mp3File, fileName, stagingStorage);
        return entityToDto(processSavingToDatabase(fileName, stagingStorage));
    }

    @Override
    public void updateStorage(long id) {
        ResourceEntity resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with id " + id + " was not found", 400));
        StorageDTO permanentStorage = storageTypeFilter.findPermanentStorage();
        processMovingInAmazonS3(resource,
                storageService.findById(resource.getStorageId(), storageTypeFilter.getJwtToken()), permanentStorage);
        resource.setStorageId(permanentStorage.getId());
        resourceRepository.save(resource);
    }

    @Override
    public List<Long> delete(List<Long> ids) {
        List<Long> deletedIds = new ArrayList<>();
        ids.forEach(id -> processDeleting(deletedIds, id));
        return deletedIds;
    }

    private void processSavingToAmazonS3(MultipartFile mp3File, String fileName, StorageDTO storageDTO) {
        amazonS3Service.putFile(storageDTO.getBucket(),
                (storageDTO.getPath() != null ? storageDTO.getPath() : "") + fileName, mp3File);
    }

    private void processMovingInAmazonS3(ResourceEntity resource, StorageDTO currentStorage,
                                         StorageDTO destinationStorage) {
        amazonS3Service.moveFile(currentStorage.getBucket(),
                (currentStorage.getPath() != null ? currentStorage.getPath() : "") + resource.getFileName(),
                destinationStorage.getBucket(),
                (destinationStorage.getPath() != null ? destinationStorage.getPath() : "") + resource.getFileName());
    }

    private ResourceEntity processSavingToDatabase(String fileName, StorageDTO storageDTO) {
        try {
            return resourceRepository.save(new ResourceEntity(0, fileName, storageDTO.getId()));
        } catch (Exception e) {
            throw new ResourceSavingException(e,
                    "File " + fileName + " wasn't saved in database! Please delete it in amazon s3", 500);
        }
    }

    private byte[] processGetting(ResourceEntity resource) {
        StorageDTO storage = storageService.findById(resource.getStorageId(), storageTypeFilter.getJwtToken());
        return amazonS3Service.getFileContent(storage.getBucket(),
                (storage.getPath() != null ? storage.getPath() : "") + resource.getFileName());
    }

    private void processDeleting(List<Long> deletedIds, Long id) {
        resourceRepository.findById(id).ifPresent(entity -> {
            StorageDTO storage = storageService.findById(entity.getStorageId(), storageTypeFilter.getJwtToken());
            amazonS3Service.deleteFile(storage.getBucket(),
                    (storage.getPath() != null ? storage.getPath() : "") + entity.getFileName());
            resourceRepository.delete(entity);
            deletedIds.add(entity.getId());
        });
    }

    private ResourceDTO entityToDto(ResourceEntity resourceEntity) {
        return modelMapper.map(resourceEntity, ResourceDTO.class);
    }

    private String getFileName(MultipartFile mp3File) {
        return System.currentTimeMillis() + "_" + mp3File.getOriginalFilename();
    }

}
