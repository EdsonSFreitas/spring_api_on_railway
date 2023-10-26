package freitas.io.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import freitas.io.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.jwt.expiration_in_minutes}")
    private int expirationInMinutes;


    public String generateToken(User user) {
        return JWT.create()
                .withIssuer("freitas.io")
                .withSubject(user.getUsername())
                .withClaim("id", user.getId().toString())
                .withExpiresAt(Date.from(LocalDateTime.now()
                        .plusMinutes(expirationInMinutes)
                        .toInstant(ZoneOffset.of("-03:00"))))
                .sign(Algorithm.HMAC256(secret));
    }

    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("freitas.io")
                .build()
                .verify(token).getSubject();
    }
}