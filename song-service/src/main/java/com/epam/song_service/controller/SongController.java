package com.epam.song_service.controller;

import com.epam.song_service.exception.impl.IdValidationException;
import com.epam.song_service.exception.impl.SongValidationException;
import com.epam.song_service.model.Song;
import com.epam.song_service.service.SongService;
import com.epam.song_service.util.Id;
import com.epam.song_service.util.Ids;
import com.epam.song_service.validator.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final IdValidator idValidator;
    private final SongService songService;

    @Autowired
    public SongController(SongService songService, IdValidator idValidator) {
        this.songService = songService;
        this.idValidator = idValidator;
    }

    @Validated
    @PostMapping
    public Id saveSong(@RequestBody @Valid Song song, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new SongValidationException("Not valid body, reason: " + bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", ")));
        }
        Song saved = songService.save(song);
        return new Id(saved.getId());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Song> findById(@PathVariable("id") long id) {
        return ResponseEntity.ok(songService.findById(id));
    }

    @DeleteMapping
    public Ids deleteByIds(@RequestParam(value = "id") String ids) {
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "");
        idValidator.validate(ids, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new IdValidationException("Not valid, reason: " + bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", ")));
        }
        List<Long> deleted = songService.deleteByIds(Arrays.stream(ids.split(","))
                .map(Long::valueOf)
                .toList());
        return new Ids(deleted);
    }
}
