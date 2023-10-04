package freitas.io.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 04/10/2023
 * {@code @project} api
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StandardError implements Serializable {
    @Serial
    private static final long serialVersionUID = -6868184536352257798L;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}