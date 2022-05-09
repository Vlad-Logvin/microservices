package com.epam.storage.configuration;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableJpaRepositories(basePackages = "com.epam.storage.persistence")
@EntityScan(basePackages = "com.epam.storage.model")
public class PersistenceDevConfiguration {
}
