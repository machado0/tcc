package com.machado0.gamificacao.trilha;

import java.util.List;

public record AulaResponseDTO(
        Long id, String nome, String descricao, Integer ordem,
        Integer duracaoMinutos, List<QuestaoResponseDTO> questoes,
        String status, Integer progressoPercentual) {
}
