package com.machado0.gamificacao.dashboard;

import java.time.LocalDate;

public record ProgressoDiaDTO(
        LocalDate data,
        String dia,
        int xp,
        int aulasConcluidas,
        int questoesRespondidas) {
}
