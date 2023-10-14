package freitas.io.dto;

import freitas.io.domain.model.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 09/10/2023
 * {@code @project} api
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private UUID id;
    @NotEmpty(message = "{field.login.obrigatorio}")
    private String login;
    private String name;
    private Account account;
    private Card card;
    private List<Feature> features;
    private List<News> news;

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.name = user.getName();
        this.account = user.getAccount();
        this.card = user.getCard();
        this.features = user.getFeatures();
        this.news = user.getNews();
    }
}