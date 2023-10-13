package freitas.io.config;

import freitas.io.domain.model.*;
import freitas.io.domain.repository.UserRepository;
import freitas.io.enums.RolesEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.TransactionSystemException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yaml")
@ExtendWith(MockitoExtension.class)
class CargaInicialTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void test_run_and_save_user() throws Exception {
        News newsTest = new News();
        newsTest.setDescription("Descrição teste");
        newsTest.setIcon("icon teste");

        Feature featuresTest = new Feature();
        featuresTest.setDescription("Feature teste");
        featuresTest.setIcon("icon teste");

        User userTest = User.builder()
                .id(null)
                .login("teste.freitas")
                .password(passwordEncoder.encode("123456"))
                .name("Teste Freitas")
                .account(Account.builder()
                        .number("123")
                        .agency("123")
                        .limit(BigDecimal.valueOf(100.00))
                        .balance(BigDecimal.valueOf(1000.00))
                        .build())
                .card(Card.builder()
                        .number("9999-9999-9999-8888")
                        .limit(BigDecimal.valueOf(500.00))
                        .build())
                .news(List.of(newsTest))
                .features(List.of(featuresTest))
                .role(RolesEnum.ROLE_USER.getCode())
                .build();

        User savedUser = userRepository.save(userTest);

        assertNotNull(savedUser.getId());
    }

    @Test
    void test_saveUserWithNullLogin() throws Exception {
        User userTest = User.builder()
                .login(null)
                .password(passwordEncoder.encode("123456"))
                .name("Edson Freitas")
                .account(Account.builder()
                        .number("777")
                        .agency("888")
                        .limit(BigDecimal.valueOf(100.00))
                        .balance(BigDecimal.valueOf(1000.00))
                        .build())
                .card(Card.builder()
                        .number("*******************")
                        .limit(BigDecimal.valueOf(500.00))
                        .build())
                .news(Collections.emptyList())
                .features(Collections.emptyList())
                .role(RolesEnum.ROLE_USER.getCode())
                .build();
        assertThrows(TransactionSystemException.class, () -> userRepository.save(userTest));

    }

    @Test
    void test_save_user_with_null_account_number() throws Exception {
        User userTest = User.builder()
                .login("fulano")
                .password(passwordEncoder.encode("123456"))
                .name("Edson Freitas")
                .account(Account.builder()
                        .number(null)
                        .agency("888")
                        .limit(BigDecimal.valueOf(100.00))
                        .balance(BigDecimal.valueOf(1000.00))
                        .build())
                .card(Card.builder()
                        .number("*******************")
                        .limit(BigDecimal.valueOf(500.00))
                        .build())
                .news(Collections.emptyList())
                .features(Collections.emptyList())
                .role(RolesEnum.ROLE_USER.getCode())
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(userTest));

    }
}