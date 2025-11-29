package com.meuportfolio.dtos;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank(message = "O campo username é obrigatório.")
        String username,

        @NotBlank(message = "O campo password é obrigatório.")
        String password
) {
    // Records são classes concisas para transporte de dados.
    // O construtor, getters, equals, hashCode e toString são gerados automaticamente.
}