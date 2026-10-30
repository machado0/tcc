package com.machado0.gamificacao.professor;

public record AlunoResumoResponseDTO(
        Long id,
        String nome,
        String email,
        Integer xp,
        Integer nivel,
        Integer streak) {
}
