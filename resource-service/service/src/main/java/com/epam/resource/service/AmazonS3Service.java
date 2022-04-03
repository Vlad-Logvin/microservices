package com.epam.resource.service;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3Service {
    boolean putObject(String bucketName, String key, MultipartFile file);

    byte[] getObject(String bucketName, String fileName);

    boolean deleteObject(String bucketName, String fileName);
}
