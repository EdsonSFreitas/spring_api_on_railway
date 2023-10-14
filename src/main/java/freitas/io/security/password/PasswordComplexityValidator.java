package freitas.io.security.password;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/09/2023
 * {@code @project} spring-vendas
 */

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordComplexityValidator implements ConstraintValidator<PasswordComplexity, String> {

    private int minLength;
    private boolean requireLowerCase;
    private boolean requireUpperCase;
    private boolean requireSpecialChar;
    private boolean requireNumber;

    @Override
    public void initialize(PasswordComplexity constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.requireLowerCase = constraintAnnotation.requireLowerCase();
        this.requireUpperCase = constraintAnnotation.requireUpperCase();
        this.requireSpecialChar = constraintAnnotation.requireSpecialChar();
        this.requireNumber = constraintAnnotation.requireNumber();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false; // A senha não pode ser nula ou vazia
        }

        if (value.length() < minLength) {
            return false; // A senha não atende ao comprimento mínimo
        }

        // Verifique se a senha atende aos critérios especificados
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasSpecialChar = false;
        boolean hasNumber = false;

        for (char c : value.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (isSpecialCharacter(c)) {
                hasSpecialChar = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            }
        }

        if (requireLowerCase && !hasLowerCase) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{field.security.complexidade.minusculo}")
                    .addConstraintViolation();
        }

        if (requireUpperCase && !hasUpperCase) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{field.security.complexidade.maiusculo}")
                    .addConstraintViolation();
        }

        if (requireSpecialChar && !hasSpecialChar) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{field.security.complexidade.especial}")
                    .addConstraintViolation();
        }

        if (requireNumber && !hasNumber) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{field.security.complexidade.numero}")
                    .addConstraintViolation();
        }

        return hasLowerCase && hasUpperCase && hasSpecialChar && hasNumber;
    }

    private boolean isSpecialCharacter(char c) {
        // Aqui você pode personalizar a lógica para verificar caracteres especiais
        String specialCharacters = "!@#$%^&*()_+";
        return specialCharacters.contains(String.valueOf(c));
    }
}