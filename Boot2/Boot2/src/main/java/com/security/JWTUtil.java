package com.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.ZonedDateTime;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    // Метод для генерации JWT
    public String generatedToken(String username) {
        // Устанавливаем срок действия токена (в данном случае, 60 минут)

        Date expirationDate = (Date) Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withIssuedAt(new Date(1))
                .withIssuer("alishev")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    // Метод для валидации JWT и извлечения информации из токена
    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        // Создаем верификатор для проверки подписи и других параметров токена
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("alishev")
                .build();

        // Проверяем токен и декодируем его содержимое
        DecodedJWT jwt = verifier.verify(token);

        // Возвращаем значение из претензии (claim) "username"
        return jwt.getClaim("username").asString();
    }
}