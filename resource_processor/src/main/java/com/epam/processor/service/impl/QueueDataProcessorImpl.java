package com.epam.processor.service.impl;

import com.epam.processor.dto.Id;
import com.epam.processor.dto.SongDTO;
import com.epam.processor.exception.QueryProcessingException;
import com.epam.processor.service.QueueDataProcessor;
import com.epam.processor.util.Mp3Parser;
import com.epam.processor.util.RestUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class QueueDataProcessorImpl implements QueueDataProcessor {

    private final RestUtil restUtil;
    private final Mp3Parser mp3Parser;

    @Value("${resource-service.url}")
    private String resourceServiceUrl;

    @Value("${song-service.url}")
    private String songServiceUrl;

    @Autowired
    public QueueDataProcessorImpl(RestUtil restUtil, Mp3Parser mp3Parser) {
        this.restUtil = restUtil;
        this.mp3Parser = mp3Parser;
    }

    @Override
    public void processResourceId(Id id) {
        ResponseEntity<byte[]> resourceServiceResponse = restUtil.get(resourceServiceUrl + id.getId(), byte[].class);
        if (resourceServiceResponse.getStatusCode().is2xxSuccessful()) {
            processSuccess(id, resourceServiceResponse);
        } else {
            processError(id);
        }
    }

    private void processSuccess(Id id, ResponseEntity<byte[]> resourceServiceResponse) {
        restUtil.post(songServiceUrl, getSongDTO(id, resourceServiceResponse), Id.class);
    }

    private void processError(Id id) {
        restUtil.delete(resourceServiceUrl + id.getId());
    }

    private SongDTO getSongDTO(Id id, ResponseEntity<byte[]> resourceServiceResponse) {
        File file = getFile(resourceServiceResponse.getBody());
        SongDTO songDTO = mp3Parser.parseFile(file);
        songDTO.setResourceId(id.getId());
        return songDTO;
    }

    private File getFile(byte[] body) {
        File file = new File("temp.mp3");
        try {
            FileUtils.writeByteArrayToFile(file, body);
        } catch (IOException e) {
            throw new QueryProcessingException("Error during filling file", "500");
        }
        return file;
    }
}
