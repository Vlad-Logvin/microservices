package com.epam.resource.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.epam.resource.service.AmazonS3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public record AmazonS3ServiceImpl(AmazonS3 amazonS3) implements AmazonS3Service {

    @Override
    public boolean putObject(String bucketName, String key, MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
            amazonS3.putObject(bucketName, key, convertedFile);
        } catch (AmazonServiceException | IOException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public byte[] getObject(String bucketName, String fileName) {
        byte[] file;
        try {
            file = amazonS3.getObject(bucketName, fileName).getObjectContent().readAllBytes();
        } catch (AmazonServiceException | IOException e) {
            System.out.println(e);
            return null;
        }
        return file;
    }

    @Override
    public boolean deleteObject(String bucketName, String fileName) {
        try {
            amazonS3.deleteObject(bucketName, fileName);
        } catch (AmazonServiceException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
}
