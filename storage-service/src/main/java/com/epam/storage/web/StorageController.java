package com.epam.storage.web;

import com.epam.storage.model.Storage;
import com.epam.storage.service.StorageService;
import com.epam.storage.service.exception.StorageServiceException;
import com.epam.storage.service.exception.storage.StorageValidationException;
import com.epam.storage.service.exception.validation.IdValidationException;
import com.epam.storage.web.util.Id;
import com.epam.storage.web.util.Ids;
import com.epam.storage.web.validator.IdValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
@Validated
@Slf4j
@RequestMapping("/storages")
@AllArgsConstructor
public class StorageController {
    private final StorageService storageService;
    private final IdValidator idValidator;

    @GetMapping
    public List<Storage> findAll() {
        log.info("Find all storages");
        return storageService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Storage findById(@PathVariable("id") long id) {
        log.info("Find storage by id {}", id);
        return storageService.findById(id);
    }

    @PostMapping
    public Id save(@RequestBody @Valid Storage storage, BindingResult bindingResult) {
        checkHasErrors(bindingResult, StorageValidationException::new);
        log.info("Save storage {}", storage);
        return new Id(storageService.save(storage).getId());
    }

    @DeleteMapping
    public Ids delete(@RequestParam String ids) {
        validate(ids, idValidator, IdValidationException::new);
        log.info("Delete storage by ids {}", ids);
        return new Ids(storageService.deleteByIds(getIds(ids)));
    }

    private void validate(Object obj, Validator validator,
                          Supplier<? extends StorageServiceException> exceptionToThrow) {
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "");
        validator.validate(obj, bindingResult);
        checkHasErrors(bindingResult, exceptionToThrow);
    }

    private void checkHasErrors(BindingResult bindingResult,
                                Supplier<? extends StorageServiceException> exceptionToThrow) {
        if (bindingResult.hasErrors()) {
            StorageServiceException exception = exceptionToThrow.get();
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
