package freitas.io.exceptions;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 04/10/2023
 * {@code @project} api
 */
@RestControllerAdvice
@Order(2)
public class SecurityExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityExceptions(Exception ex) {
        ProblemDetail errorDetail = null;
        String properName = "access_denied_reason";

        if (ex instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(401),
                    ex.getMessage());
            errorDetail.setProperty(properName, "Authentication Failure!");
        }

        if (ex instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403),
                    ex.getMessage());
            errorDetail.setProperty(properName, "Not Authorized!");
        }

        if (ex instanceof TokenExpiredException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403),
                    ex.getMessage());
            errorDetail.setProperty(properName, "Token Expired already!");
        }

        if (ex instanceof JWTDecodeException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403),
                    ex.getMessage());
            errorDetail.setProperty(properName, "JWT Token already expired!");
        }

        if (ex instanceof SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403),
                    ex.getMessage());
            errorDetail.setProperty(properName, "JWT Signature not valid!");
        }

        if (ex instanceof SignatureVerificationException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403),
                    ex.getMessage());
            errorDetail.setProperty(properName, "JWT Signature not valid!");
        }

        return errorDetail;
    }
}