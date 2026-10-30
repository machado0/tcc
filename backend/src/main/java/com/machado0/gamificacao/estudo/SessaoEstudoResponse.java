package com.machado0.gamificacao.estudo;

import java.time.LocalDateTime;

public record SessaoEstudoResponse(
        Long id,
        Long aulaId,
        LocalDateTime inicio,
        LocalDateTime fim,
        Integer duracaoMinutos,
        boolean concluida) {
}
