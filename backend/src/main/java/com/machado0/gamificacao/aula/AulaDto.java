package com.machado0.gamificacao.aula;

import lombok.Data;

@Data
public class AulaDto {
    private Long id;
    private Long moduloId;
    private String nome;
    private String descricao;
    private int ordem;
}
