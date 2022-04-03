package com.epam.resource.repository;

import com.epam.resource.entity.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {
    List<ResourceEntity> deleteByIdIn(List<Long> ids);
}
