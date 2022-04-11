package com.epam.processor.util;

import com.epam.processor.exception.impl.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Component
public record RestUtil(RestTemplate restTemplate, MapHelper mapHelper) {
    @Autowired
    public RestUtil {
    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        try {
            return restTemplate.getForEntity(url, responseType);
        } catch (RestClientException e) {
            throw new RestException(e, "Exception was thrown during getting data from " + url, 500);
        }
    }

    public <T> ResponseEntity<T> post(String url, Object object, Class<T> responseType) {
        try {
            return restTemplate.postForEntity(url, new HttpEntity<>(getRequestBody(object), getHttpHeadersForJson()), responseType);
        } catch (RestClientException e) {
            throw new RestException(e, "Exception was thrown during posting data to " + url + ", data: " + object.toString(), 500);
        }
    }

    private Map<String, Object> getRequestBody(Object requestObject) {
        return mapHelper.convertToMap(requestObject);
    }

    private HttpHeaders getHttpHeadersForJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

}
