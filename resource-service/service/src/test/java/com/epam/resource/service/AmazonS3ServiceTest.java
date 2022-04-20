package com.epam.resource.service;

import com.amazonaws.services.s3.AmazonS3;
import com.epam.resource.configuration.ServiceTestConfiguration;
import com.epam.resource.exception.ResourceServiceException;
import com.epam.resource.exception.impl.AmazonException;
import com.epam.resource.repository.ResourceRepository;
import com.epam.resource.service.impl.AmazonS3ServiceImpl;
import com.epam.resource.service.util.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest(classes = ServiceTestConfiguration.class)
@ActiveProfiles("test")
@Tag("integration-test")
class AmazonS3ServiceTest {

    private AmazonS3Service amazonS3Service;

    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private FileUtil fileUtil;
    @MockBean
    private ResourceRepository resourceRepository;

    @Value("${amazon.s3.bucket-name}")
    private String bucketName;

    private MultipartFile file;

    @BeforeEach
    void setUp() {
        amazonS3Service = new AmazonS3ServiceImpl(amazonS3, fileUtil);
        file = new MockMultipartFile("test.mp3", new byte[]{1});
    }

    @Test
    void putFile_sendFileToS3_doesNotThrow() {
        //when
        final Executable executable = () -> amazonS3Service.putFile(bucketName, file.getName(), file);

        //then
        Assertions.assertDoesNotThrow(executable);
    }

    @Test
    void putFile_invalidBucketName_throwsAmazonException() {
        //when
        final Executable executable = () -> amazonS3Service.putFile("invalidBucket", file.getName(), file);
        final Class<? extends ResourceServiceException> expectedType = AmazonException.class;

        //then
        Assertions.assertThrows(expectedType, executable);
    }

    @Test
    void getFileContent_getExistingFile_returnsFileBytes() {
        //given
        amazonS3Service.putFile(bucketName, file.getName(), file);

        //when
        final byte[] expected = new byte[]{1};
        final byte[] actual = amazonS3Service.getFileContent(bucketName, file.getName());

        //then
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void getFileContent_getNonExistingFile_throwsAmazonException() {
        //given
        amazonS3.deleteObject(bucketName, file.getName());

        //when
        final Class<? extends ResourceServiceException> expectedType = AmazonException.class;
        final Executable executable = () -> amazonS3Service.getFileContent(bucketName, file.getName());

        //then
        Assertions.assertThrows(expectedType, executable);
    }

    @Test
    void deleteFile_deleteFileFromS3_doesNotThrow() {
        //when
        final Executable executable = () -> amazonS3Service.deleteFile(bucketName, file.getName());

        //then
        Assertions.assertDoesNotThrow(executable);
    }

    @Test
    void deleteFile_invalidBucketName_throwsAmazonException() {
        //when
        final Executable executable = () -> amazonS3Service.deleteFile("invalidBucket", file.getName());
        final Class<? extends ResourceServiceException> expectedType = AmazonException.class;

        //then
        Assertions.assertThrows(expectedType, executable);
    }
}
