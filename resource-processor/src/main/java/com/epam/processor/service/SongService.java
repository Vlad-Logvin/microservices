package com.epam.processor.service;

import com.epam.processor.dto.Id;
import com.epam.processor.dto.SongDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("song-service")
public interface SongService {
    @PostMapping("/songs")
    Id saveSong(SongDTO songDTO);
}
