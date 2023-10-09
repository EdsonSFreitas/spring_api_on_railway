package freitas.io.controller;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 07/10/2023
 * {@code @project} api
 */

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String getHome(){
        return "Acesso liberado para todos!";
    }

}