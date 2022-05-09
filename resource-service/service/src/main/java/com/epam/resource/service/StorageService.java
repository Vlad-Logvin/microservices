package com.epam.resource.service;

import com.epam.resource.dto.AuthorizationToken;
import com.epam.resource.dto.StorageDTO;
import com.epam.resource.exception.impl.StorageServiceException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@FeignClient(name = "storage-service", url = "${gateway.url}")
public interface StorageService {
    @GetMapping("/storages")
    List<StorageDTO> findAll(@RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/storages/{id}")
    StorageDTO findById(@PathVariable("id") long id,
                        @RequestHeader(value = "Authorization") String authorizationHeader);

    @Component
    class StorageTypeFilter {
        private final StorageService storageService;
        private StorageDTO lastStagingStorage;
        private StorageDTO lastPermanentStorage;
        private RestTemplate restTemplate;
        private AuthorizationToken authorizationToken = null;

        @Value("jwt.url")
        private String jwtUrl;

        @Autowired
        public StorageTypeFilter(StorageService storageService, RestTemplate restTemplate) {
            this.storageService = storageService;
            this.restTemplate = restTemplate;
        }

        @CircuitBreaker(name = "staging_storage", fallbackMethod = "getLastStagingStorage")
        public StorageDTO findStagingStorage() {
            if (authorizationToken == null) {
                requestAuthToken();
            }
            lastStagingStorage = storageService.findAll(authorizationToken.getJwt()).stream()
                    .filter(storageDTO -> storageDTO.getStorageType() == StorageDTO.StorageType.STAGING).findAny()
                    .orElseThrow(() -> new StorageServiceException("No staging storage was found!", 500));
            return lastStagingStorage;
        }

        @CircuitBreaker(name = "permanent_storage", fallbackMethod = "getLastPermanentStorage")
        public StorageDTO findPermanentStorage() {
            if (authorizationToken == null) {
                requestAuthToken();
            }
            lastPermanentStorage = storageService.findAll(authorizationToken.getJwt()).stream()
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

        public void requestAuthToken() {
            ResponseEntity<Object> entity = restTemplate.getForEntity(jwtUrl, Object.class,
                    Map.of("username", "user", "password", "user"));
            if (entity.getStatusCode().is2xxSuccessful()) {
                authorizationToken = new AuthorizationToken(entity.getHeaders().getFirst("Authorization"));
            }
        }

        public String getJwtToken() {
            if (authorizationToken == null) {
                requestAuthToken();
            }
            return authorizationToken.getJwt();
        }
    }
}
