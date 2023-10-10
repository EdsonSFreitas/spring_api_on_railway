package freitas.io.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import freitas.io.domain.repository.UserRepository;
import freitas.io.exceptions.StandardError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/10/2023
 * {@code @project} api
 */

@Component
@ControllerAdvice
@RequiredArgsConstructor
public class FilterToken extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, TokenExpiredException {
        String token;
        final String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.replace("Bearer ", "");
                final String subject = this.tokenService.getSubject(token);

                var usuario = this.repository.findByLogin(subject);

                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        filterChain.doFilter(request, response);
    }

    /*@ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String error = "Acesso negado usando classe FilterToken.";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardError> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        String error = "Erro de autenticação usando classe FilterToken.";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }*/

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<StandardError> handleTokenExpiredException(TokenExpiredException ex, HttpServletRequest request) {
        String error = "The Token has expired usando classe FilterToken.";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<StandardError> handleJWTDecodeException(JWTDecodeException ex, HttpServletRequest request) {
        String error = "The token was expected usando classe FilterToken.";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }



}