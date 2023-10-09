package freitas.io.controller;

import freitas.io.domain.model.User;
import freitas.io.dto.LoginDTO;
import freitas.io.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/10/2023
 * {@code @project} api
 */

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;


    @PostMapping("/login")
    public String auth(@RequestBody LoginDTO loginDTO) {
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.password());

        final Authentication authenticate = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        final User usuarioAutenticado = (User) authenticate.getPrincipal();

        return tokenService.generateToken(usuarioAutenticado);
    }
}