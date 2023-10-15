package freitas.io.service;

import freitas.io.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

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


}