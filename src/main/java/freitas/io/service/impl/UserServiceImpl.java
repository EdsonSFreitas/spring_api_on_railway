package freitas.io.service.impl;

import freitas.io.domain.model.User;
import freitas.io.domain.repository.UserRepository;
import freitas.io.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/10/2023
 * {@code @project} api
 */

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User create(User userToCreate) {
        if (userToCreate.getId() == null) {
            // ID está ausente, então é uma criação de novo usuário
            if (repository.existsByAccountNumber(userToCreate.getAccount().getNumber())) {
                throw new DuplicateKeyException(userToCreate.getAccount().getNumber());
            }
        } else {
            // ID não é nulo, então é uma atualização
            if (repository.existsById(userToCreate.getId())) {
                throw new DataIntegrityViolationException(userToCreate.getId().toString());
            }
        }
        return repository.save(userToCreate);
    }
}