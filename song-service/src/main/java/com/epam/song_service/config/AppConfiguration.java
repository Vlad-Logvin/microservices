package com.epam.song_service.config;

import com.epam.song_service.repository.SongRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan("com.epam.song_service")
@ComponentScan("com.epam.song_service")
@EnableJpaRepositories(basePackageClasses = SongRepository.class)
@EnableWebMvc
public class AppConfiguration {

}
