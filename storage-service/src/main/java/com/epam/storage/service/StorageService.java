package com.epam.storage.service;

import com.epam.storage.model.Storage;

import java.util.List;

public interface StorageService {
    Storage save(Storage storage);

    List<Storage> findAll();

    Storage findById(long id);

    List<Long> deleteByIds(List<Long> ids);
}
