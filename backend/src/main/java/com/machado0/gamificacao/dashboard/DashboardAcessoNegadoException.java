package com.machado0.gamificacao.dashboard;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DashboardAcessoNegadoException extends RuntimeException {
    public DashboardAcessoNegadoException() {
        super("Apenas alunos possuem dashboard");
    }
}
