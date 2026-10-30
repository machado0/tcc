package com.machado0.gamificacao.desafio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class SemVidasDisponiveisException extends RuntimeException {
    public SemVidasDisponiveisException() {
        super("O aluno nao possui vidas disponiveis");
    }
}
