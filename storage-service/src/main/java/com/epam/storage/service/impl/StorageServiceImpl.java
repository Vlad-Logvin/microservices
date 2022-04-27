package com.epam.storage.service.impl;

import com.epam.storage.model.Storage;
import com.epam.storage.persistence.StorageRepository;
import com.epam.storage.service.StorageService;
import com.epam.storage.service.exception.storage.StorageDeletingException;
import com.epam.storage.service.exception.storage.StorageFindingException;
import com.epam.storage.service.exception.storage.StorageNotFoundException;
import com.epam.storage.service.exception.storage.StorageSavingException;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class StorageServiceImpl implements StorageService {
    public static final String ALL_STORAGES = "ALL_STORAGES";
    public static final String STORAGE = "STORAGE";
    private final StorageRepository storageRepository;
    private final CacheManager cacheManager;

    @Override
    public Storage save(Storage storage) {
        try {
            clearCache();
            return storageRepository.save(storage);
        } catch (DataIntegrityViolationException e) {
            throw new StorageSavingException(e,
                    "Error occurred during saving storage to database " + storage + ". Reason: " +
                            Objects.requireNonNull(e.getRootCause()).getMessage(), 500);
        } catch (Exception e) {
            throw new StorageSavingException(e,
                    "Error occurred during saving storage to database " + storage + ". Reason: " + e.getMessage(), 500);
        }
    }

    @Override
    @Cacheable(ALL_STORAGES)
    public List<Storage> findAll() {
        try {
            return storageRepository.findAll();
        } catch (Exception e) {
            throw new StorageFindingException(e,
                    "Error occurred during finding all storages from database. Reason: " + e.getMessage(), 500);
        }
    }

    @Override
    @Cacheable(STORAGE)
    public Storage findById(long id) {
        try {
            return storageRepository.findById(id)
                    .orElseThrow(() -> new StorageNotFoundException("Storage with id " + id + " was not found", 400));
        } catch (Exception e) {
            throw new StorageFindingException(e,
                    "Error occurred during finding storage by id " + id + " from database. Reason: " + e.getMessage(),
                    500);
        }
    }

    @Override
    public List<Long> deleteByIds(List<Long> ids) {
        try {
            clearCache();
            return storageRepository.deleteByIdIn(ids)
                    .stream().map(Storage::getId)
                    .toList();
        } catch (Exception e) {
            throw new StorageDeletingException(e, "Error occurred during deleting storages with ids " + ids.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", ")) + " from database. Reason: " + e.getMessage(), 500);
        }
    }

    private void clearCache() {
        try {
            Objects.requireNonNull(cacheManager.getCache(ALL_STORAGES)).clear();
        } catch (Exception ignored) {
        }
        try {
            Objects.requireNonNull(cacheManager.getCache(STORAGE)).clear();
        } catch (Exception ignored) {
        }
    }
}
