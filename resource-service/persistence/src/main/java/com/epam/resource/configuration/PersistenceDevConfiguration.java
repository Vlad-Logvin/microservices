package com.epam.resource.configuration;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan("com.epam.resource")
@ComponentScan("com.epam.resource")
@EnableJpaRepositories(basePackages = "com.epam.resource.repository")
public class PersistenceDevConfiguration {

}
