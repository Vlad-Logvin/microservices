package com.epam.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {

    private JwtConfiguration jwtConfiguration;
    private SecretKey secretKey;
    private JwtExceptionHandler jwtExceptionHandling;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.isEmpty(authorizationHeader) ||
                !authorizationHeader.startsWith(jwtConfiguration.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.replace(jwtConfiguration.getTokenPrefix(), "");

        try {
            Claims body = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(body.getSubject(), //username
                            null,
                            //Set<SimpleGrantedAuthority>
                            ((List<Map<String, String>>) body.get("authorities")).stream()
                                    .map(map -> new SimpleGrantedAuthority(map.get("authority")))
                                    .collect(Collectors.toSet())));
        } catch (UnsupportedJwtException unsupportedJwtException) {
            jwtExceptionHandling.handleJwtException("Format does not match the format expected by the application", 403,
                    response);
        } catch (MalformedJwtException malformedJwtException) {
            jwtExceptionHandling.handleJwtException("Jwt not correctly constructed and should be rejected", 403,
                    response);
        } catch (SignatureException exception) {
            jwtExceptionHandling.handleJwtException("Jwt signature validation fails", 403, response);
        } catch (ExpiredJwtException exception) {
            jwtExceptionHandling.handleJwtException("Jwt has an expiration time before the time this method is invoked",
                    403, response);
        } catch (IllegalArgumentException exception) {
            jwtExceptionHandling.handleJwtException("Jwt string is null or has only whitespaces", 403, response);
        }

        filterChain.doFilter(request, response);
    }
}
