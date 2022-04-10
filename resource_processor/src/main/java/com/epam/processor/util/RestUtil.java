package com.epam.processor.util;

import com.epam.processor.exception.ObjectParsingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public record RestUtil(RestTemplate restTemplate, MapHelper mapHelper) {
    @Autowired
    public RestUtil {
    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType);
    }

    public <T> ResponseEntity<T> post(String url, Object object, Class<T> responseType) {
        return restTemplate.postForEntity(url, new HttpEntity<>(getRequestBody(object), getHttpHeadersForJson()), responseType);
    }

    public void delete(String url) {
        restTemplate.delete(url);
    }

    private Map<String, Object> getRequestBody(Object requestObject) {
        try {
            return mapHelper.convertToMap(requestObject);
        } catch (IllegalAccessException e) {
            throw new ObjectParsingException("Object " + requestObject + " doesn't parse to map", "500");
        }
    }

    private HttpHeaders getHttpHeadersForJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

}
