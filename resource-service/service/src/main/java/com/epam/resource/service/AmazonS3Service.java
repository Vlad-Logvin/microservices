package com.epam.resource.service;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3Service {
    void putFile(String bucketName, String key, MultipartFile file);

    byte[] getFileContent(String bucketName, String fileName);

    void deleteFile(String bucketName, String fileName);
}
