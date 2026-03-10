package com.barbearia.agendacdb.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.barbearia.agendacdb.models.Barbeiro;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret; 

    public String generateToken(Barbeiro barbeiro) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("agenda-cdb")
                    .withSubject(barbeiro.getCpf())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-03:00"));
    }
}
