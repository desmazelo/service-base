package com.meuportfolio.dtos;

import com.meuportfolio.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest (
        @NotBlank
        String username,

        @NotBlank
        String password,

        @NotBlank
        @Email
        String email,

        @NotNull
        UserRole role
){}
