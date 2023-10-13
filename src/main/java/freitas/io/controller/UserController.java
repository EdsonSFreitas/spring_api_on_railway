package freitas.io.controller;

import freitas.io.domain.model.User;
import freitas.io.dto.UserDTO;
import freitas.io.enums.RolesEnum;
import freitas.io.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/10/2023
 * {@code @project} api
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements Serializable {
    @Serial
    private static final long serialVersionUID = -7594531015430412292L;

    private final transient UserService service;

    @GetMapping("/search")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable UUID id) {
        Optional<User> optionalUser = service.findById(id);
        return optionalUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping()
    @RolesAllowed({"USER"})
    public ResponseEntity<User> findByIdParam(@RequestParam(value = "id") UUID id) {
        Optional<User> optionalUser = service.findById(id);
        return optionalUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping()
    public ResponseEntity<UserDTO> create(@Valid @RequestBody User userToCreate) {
        final UserDTO userDTO = service.create(userToCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(userDTO);
    }
}