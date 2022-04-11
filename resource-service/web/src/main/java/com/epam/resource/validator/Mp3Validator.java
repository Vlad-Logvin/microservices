package com.epam.resource.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Mp3Validator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MultipartFile.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof MultipartFile file) {
            validateContentType(errors, file);
            validateExtension(errors, file);
        } else {
            processInvalid(target, errors);
        }
    }

    private void validateContentType(Errors errors, MultipartFile file) {
        if (!"audio/mpeg".equals(file.getContentType())) {
            errors.reject("400", "Content type should be audio/mpeg");
        }
    }

    private void validateExtension(Errors errors, MultipartFile file) {
        if (file.getOriginalFilename() == null || !file.getOriginalFilename().endsWith(".mp3")) {
            errors.reject("400", "File doesn't have mp3 extension");
        }
    }

    private void processInvalid(Object target, Errors errors) {
        if (target != null) {
            errors.reject("400", "File doesn't cast to mp3");
        } else {
            errors.reject("400", "Empty request body");
        }
    }
}
