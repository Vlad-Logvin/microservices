package com.epam.song;

import com.epam.song_service.config.AppConfiguration;
import com.epam.song_service.controller.SongController;
import com.epam.song_service.model.Song;
import com.epam.song_service.repository.SongRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@SpringBootTest(classes = AppConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Tag("contract-test")
@AutoConfigureMessageVerifier
@DirtiesContext
public class SongContractTest {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongController songController;

    private long id;

    @BeforeEach
    public void setUp() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup(songController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
        id = songRepository.save(new Song(1, "First", "Second", "Third", "2:59", 123L, "2021")).getId();
    }

    @Test
    public void validate_shouldReturnEvenWhenRequestParamIsEven() throws JsonProcessingException {
        // given:
        MockMvcRequestSpecification request = given();

        // when:
        ResponseOptions<MockMvcResponse> response = given().spec(request)
                .get("/songs/" + id);

        // then:
        SpringCloudContractAssertions.assertThat(response.statusCode()).isEqualTo(200);

        // and:
        String responseBody = response.getBody().asString();
        ResponseEntity<Song> byId = songController.findById(id);
        ObjectWriter ow = new ObjectMapper().writer();
        SpringCloudContractAssertions.assertThat(responseBody).isEqualTo(ow.writeValueAsString(byId.getBody()));
    }
}
