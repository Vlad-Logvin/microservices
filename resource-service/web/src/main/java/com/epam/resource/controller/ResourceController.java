package com.epam.resource.controller;

import com.epam.resource.configuration.MessageConfiguration;
import com.epam.resource.dto.ResourceDTO;
import com.epam.resource.exception.impl.IdValidationException;
import com.epam.resource.exception.impl.ResourceNotValidException;
import com.epam.resource.service.ResourceService;
import com.epam.resource.util.Id;
import com.epam.resource.util.Ids;
import com.epam.resource.validator.IdValidator;
import com.epam.resource.validator.Mp3Validator;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resources")
public class ResourceController {
    @Autowired
    public ResourceController(ResourceService resourceService, Mp3Validator mp3Validator, IdValidator idValidator, RabbitTemplate template) {
        this.resourceService = resourceService;
        this.mp3Validator = mp3Validator;
        this.idValidator = idValidator;
        this.template = template;
    }

    private final ResourceService resourceService;
    private final Mp3Validator mp3Validator;
    private final IdValidator idValidator;
    private final RabbitTemplate template;

    @PostMapping
    public Id save(@RequestParam(value = "file") MultipartFile mp3File) {
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "");
        mp3Validator.validate(mp3File, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ResourceNotValidException("Not valid, reason: " + bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", ")));
        }
        ResourceDTO saved = resourceService.save(mp3File);
        Id id = new Id(saved.getId());
        template.convertAndSend(MessageConfiguration.RESOURCE_SERVICE_EXCHANGE, MessageConfiguration.RESOURCE_SERVICE_KEY, id);
        return id;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> findMp3File(@PathVariable long id) {
        return ResponseEntity.ok(resourceService.findById(id));
    }

    @DeleteMapping
    public Ids deleteMp3Files(@RequestParam("id") String ids) {
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "");
        idValidator.validate(ids, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new IdValidationException("Not valid, reason: " + bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", ")));
        }
        List<Long> deleted = resourceService.delete(
                Arrays.stream(ids.split(",")).map(Long::parseLong).toList());
        return new Ids(deleted);
    }

}
