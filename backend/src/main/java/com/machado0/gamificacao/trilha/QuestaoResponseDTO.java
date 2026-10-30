package com.machado0.gamificacao.trilha;

import java.util.List;

public record QuestaoResponseDTO(
        Long id, String cabecalho, String dificuldade, Integer xpRecompensa,
        List<AlternativaResponseDTO> alternativas) {
}
