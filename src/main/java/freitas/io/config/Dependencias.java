package freitas.io.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 21/10/2023
 * {@code @project} api
 */
@Configuration
public class Dependencias {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}