package freitas.io.service;

import freitas.io.domain.model.User;
import freitas.io.dto.UserDTO;

import java.util.List;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/10/2023
 * {@code @project} api
 */

public interface UserService extends CRUDService <UUID, User, UserDTO> {

    //UserDTO findById(UUID id);

    //UserDTO create(User userToCreate);

    List<User> findAll();
}