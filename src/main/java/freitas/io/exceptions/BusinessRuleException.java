package freitas.io.exceptions;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 02/09/2023
 * {@code @project} spring-vendas
 */

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}