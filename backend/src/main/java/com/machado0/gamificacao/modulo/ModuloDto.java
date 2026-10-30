package com.machado0.gamificacao.modulo;

import lombok.Data;

@Data
public class ModuloDto {
    private Long id;
    private Long cursoId;
    private String nome;
    private String descricao;
    private int ordem;
}
