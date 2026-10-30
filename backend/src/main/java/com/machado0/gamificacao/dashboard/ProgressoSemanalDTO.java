package com.machado0.gamificacao.dashboard;

import java.util.List;

public record ProgressoSemanalDTO(
        List<ProgressoDiaDTO> dias,
        int totalXp,
        int diasEstudados) {
}
