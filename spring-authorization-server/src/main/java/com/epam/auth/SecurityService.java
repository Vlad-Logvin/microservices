package com.epam.auth;

import com.epam.auth.jwt.JwtConfiguration;
import com.epam.auth.jwt.JwtExceptionHandler;
import com.epam.auth.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@AllArgsConstructor
public class SecurityService {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtExceptionHandler jwtExceptionHandler;
    private final JwtConfiguration jwtConfiguration;
    private final SecretKey secretKey;

    public void login(HttpServletRequest request, HttpServletResponse response,
                      String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, password, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            String token = jwtConfiguration.getTokenPrefix() + new JwtUsernameAndPasswordAuthenticationFilter(
                    authenticationManager, jwtConfiguration, secretKey, jwtExceptionHandler).getToken(auth);
            response.addHeader(HttpHeaders.AUTHORIZATION, token);
        }
    }

}
