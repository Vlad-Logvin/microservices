package com.epam.processor.service;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("resource-service")
public interface ResourceService {
    @GetMapping("/resources/{id}")
    Response findMp3File(@PathVariable long id);
}
