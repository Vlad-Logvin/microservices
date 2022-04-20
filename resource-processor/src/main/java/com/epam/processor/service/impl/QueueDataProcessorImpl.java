package com.epam.processor.service.impl;

import com.epam.processor.dto.Id;
import com.epam.processor.dto.SongDTO;
import com.epam.processor.exception.impl.ResourceServiceException;
import com.epam.processor.service.QueueDataProcessor;
import com.epam.processor.service.ResourceService;
import com.epam.processor.service.SongService;
import com.epam.processor.util.FileUtil;
import com.epam.processor.util.Mp3Parser;
import feign.Response;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@AllArgsConstructor
public class QueueDataProcessorImpl implements QueueDataProcessor {

    private final Mp3Parser mp3Parser;
    private final FileUtil fileUtil;
    private final ResourceService resourceService;
    private final SongService songService;

    @Override
    public void processResourceId(Id id) {
        Response response = resourceService.findMp3File(id.getId());
        if (response.status() == 200) {
            try {
                processSuccess(id, response.body().asInputStream().readAllBytes());
            } catch (IOException e) {
                throw new ResourceServiceException(e, "Exception occurred during getting body as input stream", 500);
            }
        } else {
            processError(id);
        }
    }

    private void processSuccess(Id id, byte[] responseBody) {
        songService.saveSong(getSongDTO(id, responseBody));
    }

    private void processError(Id id) {
        throw new ResourceServiceException("Exception occurred due to impossibility of getting resource with id " + id, 500);
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
