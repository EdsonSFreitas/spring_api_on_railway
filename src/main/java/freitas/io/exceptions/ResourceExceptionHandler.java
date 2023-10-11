package freitas.io.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 04/10/2023
 * {@code @project} api
 */

@RestControllerAdvice
@Order(1)
public class ResourceExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ResourceExceptionHandler.class);

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<StandardError> handleAuthenticationException(Exception ex, HttpServletRequest request) {
        String error = "Authentication failed at controller advice via classe ResourExceptionHanlder";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String error = "Acesso negado.";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> handlerDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        String error = "Data Integrity Violation Exception.";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<StandardError> handlerDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String error = "Duplicate Key Exception error, this account number already exists.";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    //Exibir apenas os campos que faltam e a mensagem correspondente ao campo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> tratarErro400(MethodArgumentNotValidException ex) {
        List<DadosErroValidacao> erros = ex.getFieldErrors().stream()
                .map(fieldError -> new DadosErroValidacao(fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(erros);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<StandardError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String error = "Meio de requisição não suportado: " + ex.getMessage();
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandardError> handleBusinessRuleException(BusinessRuleException ex, HttpServletRequest request) {
        String error = "Error business rule: " + ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableAndError400(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        if (mostSpecificCause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) mostSpecificCause;
            if (invalidFormatException.getTargetType() == BigDecimal.class) {
                String error = "O campo " + invalidFormatException.getPath() + " deve ser um valor decimal";
                HttpStatus status = HttpStatus.BAD_REQUEST;
                StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(status).body(err);
            }
        }
        return ResponseEntity.badRequest().body("Erro na requisição: " + ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String error = "Corpo da requisição ausente ou inválido";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<StandardError> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        String error = "Usuário já existe...";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

/*    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
    }*/



  /*  @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> tratarErro500(Exception ex, HttpServletRequest request) {
        // return ResponseEntity.status(HttpStatus.NO).body("Erro: " + ex.getLocalizedMessage());
        String error = "INTERNAL_SERVER_ERROR.";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }*/

    /*    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleUnexpectedException(Throwable unexpectedException) {
        var message = "Unexpected server error, see the logs.";
        logger.error(message, unexpectedException);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/


    @Getter
    private static class DadosErroValidacao {

        private final String campo;
        private final String mensagem;

        public DadosErroValidacao(String campo, String mensagem) {
            this.campo = campo;
            this.mensagem = mensagem;
        }

    }
}