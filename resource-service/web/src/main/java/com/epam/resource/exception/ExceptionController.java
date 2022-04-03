package com.epam.resource.exception;

import com.epam.resource.exception.impl.IdValidationException;
import com.epam.resource.exception.impl.ResourceNotFoundException;
import com.epam.resource.exception.impl.ResourceNotValidException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IdValidationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JSONObject idValidationException(IdValidationException e) {
        JSONObject response = new JSONObject();
        response.put("error_message", e.getMessage());
        return response;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JSONObject resourceNotFoundException(ResourceNotFoundException e) {
        JSONObject response = new JSONObject();
        response.put("error_message", e.getMessage());
        return response;
    }

    @ExceptionHandler(ResourceNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JSONObject resourceNotValidException(ResourceNotValidException e) {
        JSONObject response = new JSONObject();
        response.put("error_message", e.getMessage());
        return response;
    }
}
