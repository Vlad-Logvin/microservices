package com.epam.resource.service;

import com.epam.resource.configuration.ServiceTestConfiguration;
import com.epam.resource.dto.ResourceDTO;
import com.epam.resource.entity.ResourceEntity;
import com.epam.resource.exception.ResourceServiceException;
import com.epam.resource.exception.impl.ResourceNotFoundException;
import com.epam.resource.exception.impl.ResourceSavingException;
import com.epam.resource.repository.ResourceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServiceTestConfiguration.class)
@ActiveProfiles("test")
@Tag("unit-test")
class ResourceServiceTest {

    private ResourceService resourceService;

    @MockBean
    private AmazonS3Service amazonS3Service;

    @MockBean
    private ResourceRepository resourceRepository;

    @MockBean
    private StorageService storageService;

    @Autowired
    private ModelMapper modelMapper;

    private ResourceDTO filledResourceDTO;
    private ResourceEntity filledResourceEntity;

    private MultipartFile mp3;

    @Value("${amazon.s3.bucket-name}")
    private String testBucketName;

    @BeforeEach
    void setUp() {
        //resourceService = new ResourceServiceImpl(resourceRepository, amazonS3Service, modelMapper, storageService);

        filledResourceDTO = new ResourceDTO(1, "test.mp3", 0);
        filledResourceEntity = new ResourceEntity(1, "test.mp3", 0);
        mp3 = new MockMultipartFile("test.mp3", new byte[]{1});
    }

    @Test
    void findById_useFillResourceEntity_returnsBytes() {
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.of(filledResourceEntity));
        when(amazonS3Service.getFileContent(testBucketName, filledResourceEntity.getFileName())).thenReturn(
                new byte[]{1});

        final byte[] expected = new byte[]{1};
        final byte[] actual = resourceService.findById(1);
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void findById_useEmpty_throwsResourceNotFoundException() {
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.empty());

        final Class<? extends ResourceServiceException> expectedType = ResourceNotFoundException.class;
        final Executable executable = () -> resourceService.findById(1);
        Assertions.assertThrows(expectedType, executable);
    }

    @Test
    void save_useFillResourceEntity_returnsFillResourceDTO() {
        doNothing().when(amazonS3Service).putFile(testBucketName, mp3.getName(), mp3);
        when(resourceRepository.save(any(ResourceEntity.class))).thenReturn(filledResourceEntity);

        final ResourceDTO expected = filledResourceDTO;
        final ResourceDTO actual = resourceService.save(mp3);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_useEmptyResourceEntity_throwsResourceSavingException() {
        doNothing().when(amazonS3Service).putFile(testBucketName, mp3.getName(), mp3);
        when(resourceRepository.save(nullable(ResourceEntity.class))).thenThrow(IllegalArgumentException.class);

        final Class<? extends ResourceServiceException> expectedType = ResourceSavingException.class;
        final Executable executable = () -> resourceService.save(mp3);
        Assertions.assertThrows(expectedType, executable);
    }

    @Test
    void delete_useExistingResourceEntity_returnsListOfDeletedEntities() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(filledResourceEntity));
        when(resourceRepository.findById(2L)).thenReturn(Optional.empty());
        doNothing().when(amazonS3Service).deleteFile(testBucketName, filledResourceEntity.getFileName());
        doNothing().when(resourceRepository).delete(filledResourceEntity);

        final List<Long> expected = List.of(1L);
        final List<Long> actual = resourceService.delete(List.of(1L, 2L));
        Assertions.assertEquals(expected, actual);
    }
}
