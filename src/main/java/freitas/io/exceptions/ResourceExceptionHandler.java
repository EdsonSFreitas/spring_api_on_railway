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
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 04/10/2023
 * {@code @project} api
 */

@RestControllerAdvice
@Order(1)
public class ResourceExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ResourceExceptionHandler.class);

    private static ResponseEntity<StandardError> exceptionMessage(String error, HttpStatus status, Supplier<String> eMessageSupplier, HttpServletRequest request) {
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, eMessageSupplier.get(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<StandardError> handleAuthenticationException(Exception e, HttpServletRequest request) {
        return
                exceptionMessage("Authentication failed at controller advice",
                        HttpStatus.UNAUTHORIZED,
                        e::getMessage,
                        request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        return
                exceptionMessage("Acesso negado.",
                        HttpStatus.FORBIDDEN,
                        e::getMessage,
                        request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        return
                exceptionMessage("Resource not found.",
                        HttpStatus.NOT_FOUND,
                        e::getMessage,
                        request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> handlerDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        return
                exceptionMessage("Data Integrity Violation Exception.",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        e::getMessage,
                        request);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<StandardError> handlerDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        return
                exceptionMessage("Duplicate Key Exception error, this account number already exists.",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        e::getMessage,
                        request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> tratarErro404() {
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
    public ResponseEntity<StandardError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return
                exceptionMessage("Meio de requisição não suportado.",
                        HttpStatus.METHOD_NOT_ALLOWED,
                        e::getMessage,
                        request);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandardError> handleBusinessRuleException(BusinessRuleException e, HttpServletRequest request) {
        return
                exceptionMessage("Error business rule.",
                        HttpStatus.BAD_REQUEST,
                        e::getMessage,
                        request);
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
            if (invalidFormatException.getTargetType() == UUID.class) {
                String error = "O campo ID contem valor invalido.";
                HttpStatus status = HttpStatus.BAD_REQUEST;
                StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(status).body(err);
            }
        }
        return ResponseEntity.badRequest().body("Erro na requisição: " + ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<StandardError> handleMissingServletRequestParameter(MissingServletRequestParameterException e, HttpServletRequest request) {
        return
                exceptionMessage("Corpo da requisição ausente ou inválido.",
                        HttpStatus.BAD_REQUEST,
                        e::getMessage,
                        request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<StandardError> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        String error = "Usuário já existe...";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

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