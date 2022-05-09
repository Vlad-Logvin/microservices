package com.epam.auth;

import com.epam.auth.jwt.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.crypto.SecretKey;

@SpringBootConfiguration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private JwtConfiguration jwtConfiguration;
    private SecretKey secretKey;
    private JwtExceptionHandler jwtExceptionHandling;
    private AccessDeniedHandlerJwt accessDeniedHandlerJwt;
    private AuthenticationExceptionHandler authenticationExceptionHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable().exceptionHandling().accessDeniedHandler(accessDeniedHandlerJwt)
                .authenticationEntryPoint(authenticationExceptionHandler).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().addFilter(
                        new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfiguration, secretKey,
                                jwtExceptionHandling))
                .addFilterAfter(new JwtTokenVerifier(jwtConfiguration, secretKey, jwtExceptionHandling),
                        JwtUsernameAndPasswordAuthenticationFilter.class).authorizeRequests().antMatchers("/")
                .permitAll();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN").build(),
                User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
