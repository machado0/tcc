package com.machado0.gamificacao.desafio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DesafioInvalidoException extends RuntimeException {
    public DesafioInvalidoException(String message) {
        super(message);
    }
}
