package com.epam.resource;

import com.epam.resource.dto.ResourceDTO;
import com.epam.resource.entity.ResourceEntity;
import com.epam.resource.repository.ResourceRepository;
import com.epam.resource.service.AmazonS3Service;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Cucumber.class)
@SpringBootTest(classes = CucumberConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("component-test")
public class GetResourceStepdefs {
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

    private final Map<ResourceEntity, MultipartFile> savedEntities = new HashMap<>(2);
    private byte[] actual;

    @Given("^The following resources to get$")
    public void theFollowingResources(List<ResourceDTO> resources) {
        List<ResourceEntity> toEntities = resources.stream().map(resourceDTO -> modelMapper.map(resourceDTO, ResourceEntity.class)).toList();
        resourceRepository.saveAllAndFlush(toEntities).forEach((entity) -> {
            MockMultipartFile tempFile = new MockMultipartFile(entity.getFileName(), new byte[]{(byte) entity.getId()});
            savedEntities.put(entity, tempFile);
            amazonS3Service.putFile(bucketName, entity.getFileName(), tempFile);
        });
        Assertions.assertEquals(2, savedEntities.size());
    }

    @When("Users request data from Resource Service")
    public void usersRequestDataFromResourceService() {
        ResourceEntity entity = (ResourceEntity) savedEntities.keySet().toArray()[0];
        actual = template.getForObject("http://localhost:" + port + "/resources/" + entity.getId(), byte[].class);
    }

    @Then("Resource Service should return requested data")
    public void resourceServiceShouldReturnRequestedData() throws IOException {
        final byte[] expected = savedEntities.get(savedEntities.keySet().toArray()[0]).getBytes();
        Assertions.assertArrayEquals(expected, actual);
    }
}
