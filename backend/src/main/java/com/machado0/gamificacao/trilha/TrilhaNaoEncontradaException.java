package com.machado0.gamificacao.trilha;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TrilhaNaoEncontradaException extends RuntimeException {
    public TrilhaNaoEncontradaException(String nome) {
        super("Trilha nao encontrada: " + nome);
    }
}
