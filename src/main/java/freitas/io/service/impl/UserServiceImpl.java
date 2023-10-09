package freitas.io.service.impl;

import freitas.io.domain.model.User;
import freitas.io.domain.repository.UserRepository;
import freitas.io.dto.UserDTO;
import freitas.io.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public UserDTO create(User userToCreate) {
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
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        User createdUser = repository.save(userToCreate);

        return new UserDTO(createdUser);
    }
}