package com.machado0.gamificacao.ranking;

import java.util.List;

public record RankingResponseDTO(
        Long turmaId,
        String turmaNome,
        List<RankingItemDTO> alunos,
        RankingItemDTO meuPerfil) {
}
