package com.epam.auth.jwt;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtExceptionHandler {

    public void handleJwtException(String message, Integer statusCode, HttpServletResponse response) {
        if (!response.containsHeader("Handled")) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try {
                response.getOutputStream()
                        .println("{ \"errorMessage\": \"" + message + "\", " + "\"errorCode\": " + statusCode + " }");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            response.addHeader("Handled", "TRUE");
        }
    }
}
