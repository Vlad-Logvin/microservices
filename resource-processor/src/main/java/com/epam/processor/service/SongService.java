package com.epam.processor.service;

import com.epam.processor.dto.Id;
import com.epam.processor.dto.SongDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "song-service", url = "${gateway.url}")
public interface SongService {
    @Retryable(value = RuntimeException.class,
            maxAttemptsExpression = "2",
            backoff = @Backoff(delayExpression = "500"))
    @PostMapping("/songs")
    Id saveSong(SongDTO songDTO);
}
