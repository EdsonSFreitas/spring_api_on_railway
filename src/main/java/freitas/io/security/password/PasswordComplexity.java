package freitas.io.security.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/09/2023
 * {@code @project} spring-vendas
 */

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordComplexityValidator.class)
public @interface PasswordComplexity {

    String message() default "Invalid password complexity";

    int minLength() default 8;

    boolean requireLowerCase() default true;

    boolean requireUpperCase() default true;

    boolean requireSpecialChar() default true;

    boolean requireNumber() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}