package freitas.io.domain.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/10/2023
 * {@code @project} api
 */
@Data
@Entity(name = "tb_feature")
@Builder
@AllArgsConstructor
public class Feature extends Common {

}