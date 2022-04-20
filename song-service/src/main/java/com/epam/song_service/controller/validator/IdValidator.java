package com.epam.song_service.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;

@Component
public class IdValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof String ids) {
            validateLength(errors, ids);
            validateType(errors, ids);
        } else {
            logInvalidType(errors);
        }
    }

    private void validateLength(Errors errors, String ids) {
        if (ids.length() > 200) {
            errors.reject("500", "Parameter id length more than 200");
        }
    }

    private void validateType(Errors errors, String ids) {
        try {
            Arrays.stream(ids.split(",")).forEach(Long::parseLong);
        } catch (NumberFormatException e) {
            errors.reject("500", "Invalid ids in request query");
        }
    }

    private void logInvalidType(Errors errors) {
        errors.reject("500", "Not valid ids");
    }
}
