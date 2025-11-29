package com.meuportfolio.dtos;

public record AuthenticationResponse(
        String token
) {
    // Simplesmente armazena o token JWT gerado.
}