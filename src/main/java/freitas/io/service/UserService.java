package freitas.io.service;

import freitas.io.domain.model.User;
import freitas.io.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/10/2023
 * {@code @project} api
 */

public interface UserService extends CRUDService<UUID, User, UserDTO> {

    List<User> findAll();

    Page<UserDTO> findAllOrderBy(Pageable pageable);

    User findByLogin(String login);
}