package com.machado0.gamificacao.trilha;

import java.util.List;

public record ModuloResponseDTO(
        Long id, String nome, String descricao, Integer ordem,
        List<AulaResponseDTO> aulas, String status, Integer progressoPercentual) {
}
