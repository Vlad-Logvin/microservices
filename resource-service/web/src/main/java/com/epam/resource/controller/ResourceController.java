package com.epam.resource.controller;

import com.epam.resource.exception.ResourceServiceException;
import com.epam.resource.exception.impl.IdValidationException;
import com.epam.resource.exception.impl.ResourceNotValidException;
import com.epam.resource.publisher.ResourcePublisher;
import com.epam.resource.service.ResourceService;
import com.epam.resource.util.Id;
import com.epam.resource.util.Ids;
import com.epam.resource.validator.IdValidator;
import com.epam.resource.validator.Mp3Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceService resourceService;
    private final Mp3Validator mp3Validator;
    private final IdValidator idValidator;
    private final ResourcePublisher resourcePublisher;

    @Autowired
    public ResourceController(ResourceService resourceService, Mp3Validator mp3Validator, IdValidator idValidator, ResourcePublisher resourcePublisher) {
        this.resourceService = resourceService;
        this.mp3Validator = mp3Validator;
        this.idValidator = idValidator;
        this.resourcePublisher = resourcePublisher;
    }

    @PostMapping
    public Id save(@RequestParam(value = "file") MultipartFile mp3File) {
        validate(mp3File, mp3Validator, ResourceNotValidException::new);
        Id id = new Id(resourceService.save(mp3File).getId());
        resourcePublisher.sendToResourceProcessor(id);
        return id;
    }


    @GetMapping("/{id}")
    public ResponseEntity<byte[]> findMp3File(@PathVariable long id) {
        return ResponseEntity.ok(resourceService.findById(id));
    }

    @DeleteMapping
    public Ids deleteMp3Files(@RequestParam("id") String ids) {
        validate(ids, idValidator, IdValidationException::new);
        return new Ids(resourceService.delete(getIds(ids)));
    }

    private void validate(Object obj, Validator validator, Supplier<? extends ResourceServiceException> exceptionToThrow) {
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "");
        validator.validate(obj, bindingResult);
        if (bindingResult.hasErrors()) {
            ResourceServiceException exception = exceptionToThrow.get();
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
