package com.machado0.gamificacao.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PerfilUsuarioDTO(
        Long id,
        @NotBlank String nome,
        @NotBlank String username,
        @Email @NotBlank String email,
        String tituloHonra) {
}
