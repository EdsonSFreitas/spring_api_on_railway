package freitas.io.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 21/10/2023
 * {@code @project} api
 */

@Data
@NoArgsConstructor
// Classe não foi criada como record devido ao ModelMapper para evitar campos null no request body
public class UserStatusUpdateDTO {
    private UUID id;
    private LocalDateTime accountExpiration;
    private Boolean isAccountLocked;
    private LocalDateTime credentialsExpiration;
    private Boolean isEnabled;
}