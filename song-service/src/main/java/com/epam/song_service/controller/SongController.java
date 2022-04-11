package com.epam.song_service.controller;

import com.epam.song_service.controller.validator.IdValidator;
import com.epam.song_service.exception.SongServiceException;
import com.epam.song_service.exception.impl.IdValidationException;
import com.epam.song_service.exception.impl.SongValidationException;
import com.epam.song_service.model.Song;
import com.epam.song_service.service.SongService;
import com.epam.song_service.util.Id;
import com.epam.song_service.util.Ids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
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
        checkHasErrors(bindingResult, SongValidationException::new);
        Song saved = songService.save(song);
        return new Id(saved.getId());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Song> findById(@PathVariable("id") long id) {
        return ResponseEntity.ok(songService.findById(id));
    }

    @DeleteMapping
    public Ids deleteByIds(@RequestParam(value = "id") String ids) {
        validate(ids, idValidator, IdValidationException::new);
        return new Ids(songService.deleteByIds(getIds(ids)));
    }

    private void validate(Object obj, Validator validator, Supplier<? extends SongServiceException> exceptionToThrow) {
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "");
        validator.validate(obj, bindingResult);
        checkHasErrors(bindingResult, exceptionToThrow);
    }

    private void checkHasErrors(BindingResult bindingResult, Supplier<? extends SongServiceException> exceptionToThrow) {
        if (bindingResult.hasErrors()) {
            SongServiceException exception = exceptionToThrow.get();
            exception.setErrorMessage(getErrorMessage(bindingResult));
            exception.setErrorCode(400);
            throw exception;
        }
    }

    private String getErrorMessage(BindingResult bindingResult) {
        return "Not valid, reason: " + bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }

    private List<Long> getIds(String ids) {
        return Arrays.stream(ids.split(",")).map(Long::parseLong).toList();
    }
}
