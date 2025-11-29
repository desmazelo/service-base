package com.meuportfolio.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.meuportfolio.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera o token JWT para o usuário.
     * O 'username' é usado como "Subject" (assunto/identificador) do token.
     */
    public String generateToken(User user) {
        try {
            // Define o algoritmo de assinatura com a chave secreta
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Cria o token
            String token = JWT.create()
                    .withIssuer("meu-portfolio-api") // Emissor do token
                    .withSubject(user.getUsername()) // O usuário principal do token
                    .withExpiresAt(genExpirationDate()) // Data e hora de expiração
                    .sign(algorithm); // Assina o token

            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    /**
     * Valida o token e retorna o "Subject" (username) se for válido.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("meu-portfolio-api")
                    .build()
                    .verify(token) // Verifica a assinatura e a expiração
                    .getSubject(); // Retorna o Subject (username)
        } catch (JWTVerificationException exception) {
            // Retorna string vazia ou lança exceção se o token for inválido/expirado
            return "";
        }
    }

    /**
     * Define o tempo de expiração do token (Ex: 2 horas)
     */
    private Instant genExpirationDate() {
        // Expira em 2 horas a partir de agora, ajustado para o fuso horário de Brasília (-03:00)
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}