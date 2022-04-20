package com.epam.resource.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.epam.resource.exception.impl.AmazonException;
import com.epam.resource.service.AmazonS3Service;
import com.epam.resource.service.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public record AmazonS3ServiceImpl(AmazonS3 amazonS3, FileUtil fileUtil) implements AmazonS3Service {

    @Override
    public void putFile(String bucketName, String key, MultipartFile file) {
        File convertedFile = fileUtil.convertToFile(file);
        try {
            amazonS3.putObject(bucketName, key, convertedFile);
        } catch (SdkClientException e) {
            throw new AmazonException(e, "Exception was thrown during sending file to amazon s3 bucket", 500);
        } finally {
            fileUtil.delete(convertedFile);
        }
    }


    @Override
    public byte[] getFileContent(String bucketName, String fileName) {
        try {
            return amazonS3.getObject(bucketName, fileName).getObjectContent().readAllBytes();
        } catch (SdkClientException | IOException e) {
            throw new AmazonException(e, "Exception was thrown during getting file from amazon s3 bucket", 500);
        }
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {
        try {
            amazonS3.deleteObject(bucketName, fileName);
        } catch (SdkClientException e) {
            throw new AmazonException(e, "Exception was thrown during deleting file from amazon s3 bucket", 500);
        }
    }
}
