package freitas.io.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import freitas.io.domain.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/10/2023
 * {@code @project} api
 */

@RequiredArgsConstructor
public class FilterToken extends OncePerRequestFilter {

    private HandlerExceptionResolver exceptionResolver;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository repository;

    @Autowired
    public FilterToken(HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        final String authorizationHeader = request.getHeader("Authorization");
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.replace("Bearer ", "");
                final String subject = this.tokenService.getSubject(token);

                var usuario = this.repository.findByLogin(subject);

                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
           exceptionResolver.resolveException(request, response, null, ex);
        }
    }

}