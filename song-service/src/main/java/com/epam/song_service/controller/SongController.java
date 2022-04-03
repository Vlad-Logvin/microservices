package com.epam.song_service.controller;

import com.epam.song_service.exception.impl.IdValidationException;
import com.epam.song_service.exception.impl.SongValidationException;
import com.epam.song_service.model.Song;
import com.epam.song_service.model.SongRequest;
import com.epam.song_service.service.SongService;
import com.epam.song_service.util.Id;
import com.epam.song_service.validator.IdValidator;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Validated
@RestController
@RequestMapping("/songs")
public class SongController {

    private final IdValidator idValidator;
    private final SongService songService;
    private final ModelMapper modelMapper;

    @Autowired
    public SongController(IdValidator idValidator, SongService songService, ModelMapper modelMapper) {
        this.idValidator = idValidator;
        this.songService = songService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public Id saveSong(@RequestBody @Valid SongRequest song, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new SongValidationException("Not valid body, reason: " + bindingResult.getAllErrors());
        }
        Song saved = songService.save(modelMapper.map(song, Song.class));
        return new Id(saved.getId());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Song> findById(@PathVariable("id") long id) {
        return ResponseEntity.ok(songService.findById(id));
    }

    @DeleteMapping
    public JSONObject deleteByIds(@RequestParam(value = "id") String ids) {
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "");
        idValidator.validate(ids, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new IdValidationException("Not valid, reason: " + bindingResult.getAllErrors());
        }
        List<Long> deleted = songService.deleteByIds(
                Arrays.stream(ids.split(","))
                        .map(Long::parseLong)
                        .toList());
        JSONObject body = new JSONObject();
        body.put("ids", deleted);
        return body;
    }
}
