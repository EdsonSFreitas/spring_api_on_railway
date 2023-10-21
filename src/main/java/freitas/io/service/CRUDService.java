package freitas.io.service;

import freitas.io.dto.UserStatusRetornoDTO;
import freitas.io.dto.UserStatusUpdateDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 14/10/2023
 * {@code @project} api
 */

public interface CRUDService<ID, T, DTO> {
    List<T> findAll();
    Optional<DTO> findById(ID id);
    DTO create(T entity);
    Optional<DTO> update(ID id, DTO entity);
    void delete(ID id);

    Optional<UserStatusRetornoDTO> changeStatusUser(UUID id, UserStatusUpdateDTO entity);
}