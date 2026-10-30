package com.machado0.gamificacao.ranking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RankingNaoDisponivelException extends RuntimeException {
    public RankingNaoDisponivelException() {
        super("O aluno nao esta vinculado a uma turma ativa");
    }
}
