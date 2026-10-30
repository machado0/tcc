package com.machado0.gamificacao.auth;

public record LoginResponseDTO(
        String token,
        String tipo,
        String nome,
        String email,
        String perfil,
        Long usuarioId) {
}
