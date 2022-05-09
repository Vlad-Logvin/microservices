package com.epam.resource.repository;

import com.epam.resource.configuration.PersistenceTestConfiguration;
import com.epam.resource.entity.ResourceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

@ContextConfiguration(classes = PersistenceTestConfiguration.class)
@ActiveProfiles("test")
@Tag("integration-test")
@DataJpaTest
class ResourceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ResourceRepository resourceRepository;

    private ResourceEntity resourceEntityWithId;
    private ResourceEntity resourceEntityWithoutId;

    @BeforeEach
    void setUp() {
        resourceEntityWithId = new ResourceEntity(1L, "test1.mp3", 1L);
        resourceEntityWithoutId = new ResourceEntity("test1.mp3");
    }

    @Test
    void findById() {
        //given
        entityManager.persist(resourceEntityWithoutId);
        entityManager.flush();

        //when
        final Optional<ResourceEntity> expected = Optional.of(resourceEntityWithoutId);
        final Optional<ResourceEntity> actual = resourceRepository.findById(resourceEntityWithoutId.getId());

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save() {
        //when
        final ResourceEntity actual = resourceRepository.save(resourceEntityWithoutId);
        final ResourceEntity expected = entityManager.find(ResourceEntity.class, resourceEntityWithoutId.getId());

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete() {
        //given
        entityManager.persist(resourceEntityWithoutId);
        entityManager.flush();

        //when
        Executable executable = () -> resourceRepository.delete(resourceEntityWithoutId);

        //then
        Assertions.assertDoesNotThrow(executable);
    }
}
