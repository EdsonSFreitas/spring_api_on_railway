package freitas.io.domain.model;

import jakarta.persistence.Entity;
import lombok.Data;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/10/2023
 * {@code @project} api
 */
@Data
@Entity(name = "tb_news")
public class News extends Common {
}