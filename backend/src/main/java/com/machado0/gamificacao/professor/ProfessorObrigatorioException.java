package com.machado0.gamificacao.professor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ProfessorObrigatorioException extends RuntimeException {

    public ProfessorObrigatorioException() {
        super("A operacao exige um usuario com perfil PROFESSOR");
    }
}
