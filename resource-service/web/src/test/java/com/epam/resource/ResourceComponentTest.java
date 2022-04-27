package com.epam.resource;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.epam.resource.dto.ResourceDTO;
import com.epam.resource.repository.ResourceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty", features = "classpath:feature")
@CucumberContextConfiguration
@ActiveProfiles("test")
@SpringBootTest(classes = CucumberConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("component-test")
public class ResourceComponentTest {
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private ResourceRepository resourceRepository;
    @Value("${amazon.s3.bucket-name}")
    private String bucketName;
    @Autowired
    private ObjectMapper objectMapper;

    @DataTableType
    public ResourceDTO convert(Map<String, String> entries) {
        return new ResourceDTO(entries.get("filename"));
    }

    @Before
    public void setUp() {
        ObjectListing objectListing = amazonS3.listObjects(bucketName);
        String[] keys = objectListing.getObjectSummaries().stream().map(S3ObjectSummary::getKey).toList()
                .toArray(new String[0]);
        if (keys.length > 0) {
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(keys);
            amazonS3.deleteObjects(deleteObjectsRequest);
        }
        resourceRepository.deleteAll();
    }
}
