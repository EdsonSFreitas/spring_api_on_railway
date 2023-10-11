package freitas.io.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import freitas.io.domain.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/10/2023
 * {@code @project} api
 */
@Service
public class TokenService {
    //String secret = System.getenv("secret");

    public String generateToken(User user) {
        return JWT.create()
                .withIssuer("freitas.io")
                .withSubject(user.getUsername())
                .withClaim("id", user.getId().toString())
                .withExpiresAt(Date.from(LocalDateTime.now()
                        .plusMinutes(20)
                        .toInstant(ZoneOffset.of("-03:00"))))
                .sign(Algorithm.HMAC256("freitas.io"));
        //TODO Salvar secret como variavel de ambiente ao inves de usar como hardcoded
    }

    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256("freitas.io"))
                .withIssuer("freitas.io")
                .build()
                .verify(token).getSubject();
    }
}