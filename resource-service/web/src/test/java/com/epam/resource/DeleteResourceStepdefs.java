package com.epam.resource;

import com.epam.resource.dto.ResourceDTO;
import com.epam.resource.entity.ResourceEntity;
import com.epam.resource.exception.impl.AmazonException;
import com.epam.resource.repository.ResourceRepository;
import com.epam.resource.service.AmazonS3Service;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

@Tag("component-test")
public class DeleteResourceStepdefs {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AmazonS3Service amazonS3Service;

    @Autowired
    private TestRestTemplate template;

    @Value("${amazon.s3.bucket-name}")
    private String bucketName;

    @LocalServerPort
    private int port;

    private List<ResourceEntity> savedInstances;

    @Given("^The following resources to delete$")
    public void theFollowingResources(List<ResourceDTO> resources) {
        List<ResourceEntity> toEntities = resources.stream().map(resourceDTO -> modelMapper.map(resourceDTO, ResourceEntity.class)).toList();
        savedInstances = resourceRepository.saveAllAndFlush(toEntities);
        toEntities.forEach(entity -> amazonS3Service.putFile(bucketName, entity.getFileName(), new MockMultipartFile(entity.getFileName(), new byte[]{(byte) entity.getId()})));
        Assertions.assertEquals(2, savedInstances.size());
    }

    @When("Users wants to delete specified data in Resource Service")
    public void usersWantsToDeleteSpecifiedDataInResourceService() {
        long realId = savedInstances.get(0).getId();
        long fakeId = findPositiveNumberNotIn(savedInstances.get(0).getId(), savedInstances.get(1).getId());
        template.delete("http://localhost:" + port + "/resources?id=" + realId + "," + fakeId);
    }

    @Then("Resource Service should delete it")
    public void resourceServiceShouldDeleteIt() {
        List<ResourceEntity> all = resourceRepository.findAll();
        Assertions.assertThrows(AmazonException.class, () -> amazonS3Service.getFileContent(bucketName, savedInstances.get(0).getFileName()));
        Assertions.assertNotNull(amazonS3Service.getFileContent(bucketName, savedInstances.get(1).getFileName()));
        Assertions.assertEquals(1, all.size());
        Assertions.assertEquals(savedInstances.get(1), all.get(0));
    }

    private long findPositiveNumberNotIn(long... numbers) {
        for (long l = 1; l < Long.MAX_VALUE; l++) {
            long finalL = l;
            if (Arrays.stream(numbers).noneMatch(number -> finalL == number)) {
                return finalL;
            }
        }
        throw new RuntimeException("No positive number found");
    }
}
