package com.epam.resource.service;

import com.epam.resource.dto.StorageDTO;
import com.epam.resource.exception.impl.StorageServiceException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "storage-service", url = "${gateway.url}")
public interface StorageService {
    @GetMapping("/storages")
    List<StorageDTO> findAll();

    @GetMapping("/storages/{id}")
    StorageDTO findById(@PathVariable("id") long id);

    @Component
    class StorageTypeFilter {
        private final StorageService storageService;
        private StorageDTO lastStagingStorage;
        private StorageDTO lastPermanentStorage;

        @Autowired
        public StorageTypeFilter(StorageService storageService) {
            this.storageService = storageService;
        }

        @CircuitBreaker(name = "staging_storage", fallbackMethod = "getLastStagingStorage")
        public StorageDTO findStagingStorage() {
            lastStagingStorage = storageService.findAll().stream()
                    .filter(storageDTO -> storageDTO.getStorageType() == StorageDTO.StorageType.STAGING).findAny()
                    .orElseThrow(() -> new StorageServiceException("No staging storage was found!", 500));
            return lastStagingStorage;
        }

        @CircuitBreaker(name = "permanent_storage", fallbackMethod = "getLastPermanentStorage")
        public StorageDTO findPermanentStorage() {
            lastPermanentStorage = storageService.findAll().stream()
                    .filter(storageDTO -> storageDTO.getStorageType() == StorageDTO.StorageType.PERMANENT).findAny()
                    .orElseThrow(() -> new StorageServiceException("No permanent storage was found!", 500));
            return lastPermanentStorage;
        }

        public StorageDTO getLastStagingStorage(Throwable t) {
            return lastStagingStorage;
        }

        public StorageDTO getLastPermanentStorage(Throwable t) {
            return lastPermanentStorage;
        }
    }
}
