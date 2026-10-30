package com.machado0.gamificacao.professor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlunoDuplicadoException extends RuntimeException {

    public EmailAlunoDuplicadoException(String email) {
        super("E-mail ja cadastrado: " + email);
    }
}
