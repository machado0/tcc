package com.machado0.gamificacao.desafio;

import jakarta.validation.constraints.NotNull;

public record SubmeterDesafioDTO(
        @NotNull Long questaoId,
        @NotNull Long alternativaEscolhidaId) {
}
