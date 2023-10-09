package freitas.io.config;

import freitas.io.domain.model.*;
import freitas.io.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 07/10/2023
 * {@code @project} api
 */

@Configuration
@Profile("dev")
public class TestConfig implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        News newsTest = new News();
        newsTest.setDescription("Descrição teste");
        newsTest.setIcon("icon teste");

        Feature fetuaresTest = new Feature();
        fetuaresTest.setDescription("Feature teste");
        fetuaresTest.setIcon("icon teste");

        User userTest =  User.builder()
                .id(null)
                .login("edson.s.freitas")
                .password(passwordEncoder.encode("123456"))
                .name("Edson Freitas")
                .account(Account.builder()
                        .number("777")
                        .agency("888")
                        .limit(BigDecimal.valueOf(100.00))
                        .balance(BigDecimal.valueOf(1000.00))
                        .build())
                .card(Card.builder()
                        .number("9999-9999-9999-9999")
                        .limit(BigDecimal.valueOf(500.00))
                        .build())
                .news(List.of(newsTest))
                .features(List.of(fetuaresTest))
                .build();

        userRepository.save(userTest);
    }
}