package freitas.io.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import freitas.io.domain.model.User;
import freitas.io.domain.repository.UserRepository;
import freitas.io.dto.UserDTO;
import freitas.io.dto.UserStatusRetornoDTO;
import freitas.io.dto.UserStatusUpdateDTO;
import freitas.io.exceptions.BusinessRuleException;
import freitas.io.exceptions.ResourceNotFoundException;
import freitas.io.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
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

    @Override
    public User findByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public Optional<UserStatusRetornoDTO> changeStatusUser(UUID id, UserStatusUpdateDTO updateStatus) {
        final User userToChange = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (updateStatus.accountExpiration() != null) {
            userToChange.setAccountExpiration(updateStatus.accountExpiration());
        }
        if (updateStatus.isAccountLocked() != null) {
            userToChange.setAccountLocked(updateStatus.isAccountLocked());
        }
        if (updateStatus.credentialsExpiration() != null) {
            userToChange.setCredentialsExpiration(updateStatus.credentialsExpiration());
        }
        if (updateStatus.isEnabled() != null) {
            userToChange.setEnabled(updateStatus.isEnabled());
        }

        final User user = repository.save(userToChange);

        UserStatusRetornoDTO retornoDTO = new UserStatusRetornoDTO(
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getAccountExpiration(),
                user.isAccountLocked(),
                user.getCredentialsExpiration(),
                user.isEnabled()
        );
        return Optional.of(retornoDTO);
    }

    private void validateChangeableId(UUID id, String operation) {
        if (UNCHANGEABLE_USER_ID.equals(id)) {
            throw new BusinessRuleException("User with ID %d can not be %s.".formatted(UNCHANGEABLE_USER_ID, operation));
        }
    }

}