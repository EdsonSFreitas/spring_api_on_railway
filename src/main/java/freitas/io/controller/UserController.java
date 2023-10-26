package freitas.io.controller;

import freitas.io.domain.model.User;
import freitas.io.dto.UserCompleteDTO;
import freitas.io.dto.UserDTO;
import freitas.io.dto.UserStatusRetornoDTO;
import freitas.io.dto.UserStatusUpdateDTO;
import freitas.io.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/10/2023
 * {@code @project} api
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Users")
@RequiredArgsConstructor
public class UserController implements Serializable {
    @Serial
    private static final long serialVersionUID = -7594531015430412292L;

    private final transient UserService service;

    int limitPageSize = 100;

    @Operation(summary = "Find All", description = "find all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")}
    )
    @GetMapping({"/v1.0/user/search", "/v1.1/user/search"})
    @RolesAllowed({"USER", "ADMIN"})
    public ResponseEntity<Page<UserDTO>> getAllUsersOrderedBy(Pageable pageable) {
        if (pageable.getPageSize() > limitPageSize) {
            pageable = PageRequest.of(pageable.getPageNumber(), limitPageSize, pageable.getSort());
        }
        Page<UserDTO> users = service.findAllOrderBy(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Find User by UUID with Path Variable", description = "Find user by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")}
    )
    @GetMapping({"/v1.0/user/{id}", "/v1.1/user/{id}", "/v1.2/user/{id}"})
    public ResponseEntity<UserCompleteDTO> findById(@PathVariable UUID id) {
        final Optional<UserCompleteDTO> dto = service.findById(id);
        return dto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Find User by UUID with Parameter", description = "Find user by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")}
    )
    @GetMapping({"/v1.2/user"})
    @RolesAllowed({"USER"})
    public ResponseEntity<UserCompleteDTO> findByIdParam(@RequestParam(value = "id") UUID id) {
        Optional<UserCompleteDTO> optionalUser = service.findById(id);
        return optionalUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Register new User", description = "Create a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")}
    )
    @PostMapping({"/v1.0/user", "/v1.2/user"})
    public ResponseEntity<UserDTO> create(@Valid @RequestBody User userToCreate) {
        final UserDTO userDTO = service.create(userToCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(userDTO);
    }

    @PutMapping({"/v1.0/user/{id}", "/v1.1/user/{id}", "/v1.2/user/{id}"})
    @Operation(summary = "Update a user", description = "Update the data of an existing user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "422", description = "Invalid user data provided")
    })
    public ResponseEntity<UserDTO> update(@PathVariable UUID id, @RequestBody UserDTO userToUpdate) {
        final Optional<UserDTO> updatedUser = service.update(id, userToUpdate);
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/v1.0/user/{id}"})
    @Operation(summary = "Delete a user", description = "Delete an existing user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> delete(@RequestParam(value = "id") UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Change user status", description = "Block or Expire credential or account an existing user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Action completed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })

    @PatchMapping({"/v1.0/user/changestatus"})
    public ResponseEntity<Optional<UserStatusRetornoDTO>> changeStatusUserById(@RequestBody UserStatusUpdateDTO updateStatus) {
        final Optional<UserStatusRetornoDTO> userStatusRetornoDTO = service.changeStatusUser(updateStatus.getId(), updateStatus);
        return ResponseEntity.ok().body(userStatusRetornoDTO);
    }
}