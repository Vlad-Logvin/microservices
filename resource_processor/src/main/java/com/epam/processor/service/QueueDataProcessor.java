package com.epam.processor.service;

import com.epam.processor.dto.Id;

public interface QueueDataProcessor {
    void processResourceId(Id id);
    void processRecovering();
}
