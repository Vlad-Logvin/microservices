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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public record ResourceServiceImpl(ResourceRepository resourceRepository, AmazonS3Service amazonS3Service,
                                  ModelMapper modelMapper) implements ResourceService {

    private static String bucketName;

    @Autowired
    public ResourceServiceImpl {
    }

    @Value("${amazon.s3.bucket-name}")
    private void setBucketName(String bucketName) {
        ResourceServiceImpl.bucketName = bucketName;
    }

    @Override
    public byte[] findById(long id) {
        ResourceEntity resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with id " + id + " was not found", 400));
        return processGetting(resource);
    }

    @Override
    public ResourceDTO save(MultipartFile mp3File) {
        String fileName = getFileName(mp3File);
        processSavingToAmazonS3(mp3File, fileName);
        return entityToDto(processSavingToDatabase(fileName));
    }

    @Override
    public List<Long> delete(List<Long> ids) {
        List<Long> deletedIds = new ArrayList<>();
        ids.forEach(id -> processDeleting(deletedIds, id));
        return deletedIds;
    }


    private void processSavingToAmazonS3(MultipartFile mp3File, String fileName) {
        amazonS3Service.putFile(bucketName, fileName, mp3File);
    }

    private ResourceEntity processSavingToDatabase(String fileName) {
        try {
            return resourceRepository.save(new ResourceEntity(fileName));
        } catch (Exception e) {
            throw new ResourceSavingException(e, "File " + fileName + " wasn't saved in database! Please delete it in amazon s3", 500);
        }
    }

    private byte[] processGetting(ResourceEntity resource) {
        return amazonS3Service.getFileContent(bucketName, resource.getFileName());
    }

    private void processDeleting(List<Long> deletedIds, Long id) {
        resourceRepository.findById(id).ifPresent(entity -> {
            amazonS3Service.deleteFile(bucketName, entity.getFileName());
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
