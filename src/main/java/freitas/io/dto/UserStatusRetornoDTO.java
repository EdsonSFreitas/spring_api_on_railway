package freitas.io.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 21/10/2023
 * {@code @project} api
 */

public record UserStatusRetornoDTO(
        UUID id, String login, String name, LocalDateTime accountExpiration,Boolean isAccountLocked, LocalDateTime credentialsExpiration, Boolean isEnabled) {
    public UserStatusRetornoDTO {
    }
}