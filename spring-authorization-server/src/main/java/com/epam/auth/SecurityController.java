package com.epam.auth;

import com.epam.auth.jwt.UsernameAndPasswordAuthenticationRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
public class SecurityController {

    private SecurityService securityService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody UsernameAndPasswordAuthenticationRequest userAuthRequest) {
        securityService.login(request, response, userAuthRequest.getUsername(), userAuthRequest.getPassword());
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @PostMapping
    public ResponseEntity<Acceptance> validate() {
        return ResponseEntity.ok(new Acceptance(true));
    }
}
