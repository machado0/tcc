package com.machado0.gamificacao.professor;

import java.time.OffsetDateTime;

public record AlunoCadastradoResponseDTO(
        Long id,
        String nome,
        String email,
        OffsetDateTime dataCriacao) {
}
