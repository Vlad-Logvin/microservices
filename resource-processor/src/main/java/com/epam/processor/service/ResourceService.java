package com.epam.processor.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "resource-service", url = "${gateway.url}")
public interface ResourceService {
    @Retryable(value = RuntimeException.class,
            maxAttemptsExpression = "2",
            backoff = @Backoff(delayExpression = "500"))
    @GetMapping("/resources/{id}")
    ResponseEntity<byte[]> findMp3File(@PathVariable long id);

    @Retryable(value = RuntimeException.class,
            maxAttemptsExpression = "2",
            backoff = @Backoff(delayExpression = "500"))
    @PostMapping("/resources/{id}")
    void updateStorage(@PathVariable long id);
}
