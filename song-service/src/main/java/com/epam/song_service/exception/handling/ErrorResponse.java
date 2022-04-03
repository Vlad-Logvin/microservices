package com.epam.song_service.exception.handling;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String errorMessage;
}
