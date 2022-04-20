package com.epam.song;

import com.epam.song_service.config.AppConfiguration;
import com.epam.song_service.controller.SongController;
import com.epam.song_service.model.Song;
import com.epam.song_service.service.SongService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AppConfiguration.class)
@Tag("contract-test")
public class SongContractTest {
    @Autowired
    private SongController songController;

    @MockBean
    private SongService songService;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(songController);
        when(songService.findById(anyLong()))
                .thenReturn(new Song(1, "First", "Second", "Third", "2:59", 123L, "2021"));
    }
}
