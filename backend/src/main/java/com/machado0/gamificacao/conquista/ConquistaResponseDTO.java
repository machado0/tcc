package com.machado0.gamificacao.conquista;

import java.time.LocalDateTime;

public record ConquistaResponseDTO(
        Long id,
        String codigo,
        String titulo,
        String descricao,
        String icone,
        Integer meta,
        Integer recompensaXp,
        boolean desbloqueada,
        LocalDateTime dataDesbloqueio) {
}
