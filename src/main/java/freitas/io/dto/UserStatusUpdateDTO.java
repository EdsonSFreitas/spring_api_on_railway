package freitas.io.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 21/10/2023
 * {@code @project} api
 */

public record UserStatusUpdateDTO(
        UUID id, LocalDateTime accountExpiration, Boolean isAccountLocked, LocalDateTime credentialsExpiration,
        Boolean isEnabled) {

    public UserStatusUpdateDTO {
    }
}