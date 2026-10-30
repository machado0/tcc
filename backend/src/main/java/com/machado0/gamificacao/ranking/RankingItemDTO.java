package com.machado0.gamificacao.ranking;

public record RankingItemDTO(
        Integer posicao,
        Long usuarioId,
        String nome,
        Integer xp,
        Integer nivel,
        Integer streak) {
}
