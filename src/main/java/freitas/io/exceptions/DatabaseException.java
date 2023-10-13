package freitas.io.exceptions;

import java.io.Serial;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 04/10/2023
 * {@code @project} api
 */

public class DatabaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7416249776246764573L;

    public DatabaseException(String msg) {
        super(msg);
    }
}