package com.machado0.gamificacao.alternativa;

import lombok.Data;

@Data
public class AlternativaDto {
    private Long id;
    private Long questaoId;
    private String texto;
    private String justificativa;
    private boolean correta;
}
