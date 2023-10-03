package freitas.io.domain.repository;

import freitas.io.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/10/2023
 * {@code @project} api
 */

public interface UserRepository extends JpaRepository<User, Long> {
}