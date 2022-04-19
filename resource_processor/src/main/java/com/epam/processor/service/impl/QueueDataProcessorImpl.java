package com.epam.processor.service.impl;

import com.epam.processor.dto.Id;
import com.epam.processor.dto.SongDTO;
import com.epam.processor.exception.impl.ResourceServiceGettingException;
import com.epam.processor.service.QueueDataProcessor;
import com.epam.processor.util.FileUtil;
import com.epam.processor.util.Mp3Parser;
import com.epam.processor.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class QueueDataProcessorImpl implements QueueDataProcessor {

    private final RestUtil restUtil;
    private final Mp3Parser mp3Parser;
    private final FileUtil fileUtil;

    @Value("${resource-service.url}")
    private String resourceServiceUrl;

    @Value("${song-service.url}")
    private String songServiceUrl;

    @Autowired
    public QueueDataProcessorImpl(RestUtil restUtil, Mp3Parser mp3Parser, FileUtil fileUtil) {
        this.restUtil = restUtil;
        this.mp3Parser = mp3Parser;
        this.fileUtil = fileUtil;
    }

    @Override
    public void processResourceId(Id id) {
        ResponseEntity<byte[]> resourceServiceResponse = restUtil.get(resourceServiceUrl + id.getId(), byte[].class);
        if (resourceServiceResponse.getStatusCode().is2xxSuccessful()) {
            processSuccess(id, resourceServiceResponse.getBody());
        } else {
            processError(id);
        }
    }

    private void processSuccess(Id id, byte[] responseBody) {
        restUtil.post(songServiceUrl, getSongDTO(id, responseBody), Id.class);
    }

    private void processError(Id id) {
        throw new ResourceServiceGettingException("Exception occurred due to impossibility of getting resource with id " + id, 500);
    }

    private SongDTO getSongDTO(Id id, byte[] content) {
        SongDTO songDTO = parseFileToSongDTO(content);
        songDTO.setResourceId(id.getId());
        return songDTO;
    }

    private SongDTO parseFileToSongDTO(byte[] content) {
        File file = fileUtil.getFile(content);
        SongDTO songDTO;
        try {
            songDTO = mp3Parser.parseFile(file);
        } finally {
            fileUtil.delete(file);
        }
        return songDTO;
    }
}
