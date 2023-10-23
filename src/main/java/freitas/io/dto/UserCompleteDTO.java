package freitas.io.dto;

import freitas.io.domain.model.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 09/10/2023
 * {@code @project} api
 */

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserCompleteDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4273655462694315464L;

    private UUID id;
    private String login;
    private String name;
    private Account account;
    private Card card;
    private List<Feature> features;
    private List<News> news;

    public UserCompleteDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.name = user.getName();
        this.account = user.getAccount();
        this.card = user.getCard();
        this.features = user.getFeatures();
        this.news = user.getNews();
    }

    public UserCompleteDTO(UserCompleteDTO userDTO) {
    }
}