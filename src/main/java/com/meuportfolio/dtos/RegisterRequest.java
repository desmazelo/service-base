package com.meuportfolio.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(

        @NotBlank(message = "O nome de usuário é obrigatório.")
        String username,

        @NotBlank(message = "A senha é obrigatória.")
        String password,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @NotBlank(message = "O nome da Role é obrigatório.")
        @Pattern(regexp = "^ROLE_[A-Z]+$", message = "A Role deve seguir o formato ROLE_NOME.")
        String roleName
) { }