package freitas.io.service.impl;

import freitas.io.domain.model.User;
import freitas.io.domain.repository.UserRepository;
import freitas.io.dto.UserDTO;
import freitas.io.exceptions.BusinessRuleException;
import freitas.io.exceptions.ResourceNotFoundException;
import freitas.io.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/10/2023
 * {@code @project} api
 */

@Service
public class UserServiceImpl implements UserService {
    private static final Long UNCHANGEABLE_USER_ID = 1L;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    public Optional<UserDTO> findById(UUID id) {
        final User user = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        UserDTO userDTO = new UserDTO(user);
        return Optional.of(userDTO);
    }


    public List<User> findAll() {
        return repository.findAll();
    }

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

    @Override
    public Optional<UserDTO> update(UUID id, UserDTO userToUpdate) {
        this.validateChangeableId(id, "updated");
        userToUpdate.setId(id);
        final User dbUser = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if (!dbUser.getId().equals(userToUpdate.getId())) {
            throw new BusinessRuleException("Update IDs must be the same.");
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(userToUpdate, dbUser);
        final User saved = this.repository.save(dbUser);
        return Optional.of(new UserDTO(saved));
    }


    public void delete(UUID id) {
        this.validateChangeableId(id, "deleted");
        final User dbUser = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        this.repository.delete(dbUser);
    }

    public Page<UserDTO> findAllOrderBy(Pageable pageable) {
        Page<User> pageResult = repository.findAll(pageable);
        List<UserDTO> dtos = pageResult.getContent()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, pageResult.getTotalElements());
    }

    private void validateChangeableId(UUID id, String operation) {
        if (UNCHANGEABLE_USER_ID.equals(id)) {
            throw new BusinessRuleException("User with ID %d can not be %s.".formatted(UNCHANGEABLE_USER_ID, operation));
        }
    }

}