package freitas.io.controller;

import freitas.io.domain.model.User;
import freitas.io.dto.LoginDTO;
import freitas.io.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/10/2023
 * {@code @project} api
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Authentication")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenService tokenService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Operation(summary = "Authentication", description = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")}
    )
    @PostMapping({"/v1.0/login", "/v1.1/login"})
    public String auth(@RequestBody LoginDTO loginDTO) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.login());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, loginDTO.password(), userDetails.getAuthorities());

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User usuarioAutenticado = (User) authenticate.getPrincipal();

        return tokenService.generateToken(usuarioAutenticado);
    }
}