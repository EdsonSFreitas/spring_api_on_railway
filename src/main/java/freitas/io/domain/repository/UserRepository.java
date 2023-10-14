package freitas.io.domain.repository;

import freitas.io.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/10/2023
 * {@code @project} api
 */

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByAccountNumber(String accountNumber);

    boolean existsByCardNumber(String number);

    User findByLogin(String login);
/*
    void delete(Optional<User> dbUser);*/
}