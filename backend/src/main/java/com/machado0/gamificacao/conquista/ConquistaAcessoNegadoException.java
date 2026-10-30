package com.machado0.gamificacao.conquista;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ConquistaAcessoNegadoException extends RuntimeException {
    public ConquistaAcessoNegadoException() {
        super("Apenas alunos possuem conquistas");
    }
}
