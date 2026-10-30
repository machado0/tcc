package com.machado0.gamificacao.progresso;

import lombok.Data;

@Data
public class ProgressoDto {
    private Long id;
    private Long usuarioId;
    private Long aulaId;
    private int percentualConclusao;
}
