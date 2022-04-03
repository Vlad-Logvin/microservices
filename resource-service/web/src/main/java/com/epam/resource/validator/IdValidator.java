package com.epam.resource.validator;

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
            if (ids.length() > 200) {
                errors.reject("500", "Parameter id length more than 200");
            }
            try {
                Arrays.stream(ids.split(",")).forEach(Long::parseLong);
            } catch (NumberFormatException e) {
                errors.reject("500", "Invalid ids in request query");
            }
        } else {
            if (target != null) {
                errors.reject("500", "Ids aren't String type");
            } else {
                errors.reject("500", "No ids defined");
            }
        }
    }
}
