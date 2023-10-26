package freitas.io.controller;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 07/10/2023
 * {@code @project} api
 */

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Home", description = "Test home without authentication")
@RequestMapping("/api")
public class HomeController {

    @GetMapping({"/v1.0/home"})
    public String getHomev10(){
        return "Acesso liberado para todos sem autenticação ou autorização, versão 1.0!";
    }

    @GetMapping({"/v1.1/home"})
    public String getHomev11(){
        return "Acesso liberado para todos sem autenticação ou autorização, versão 1.1!";
    }

}