package com.machado0.gamificacao.questao;

import lombok.Data;

@Data
public class QuestaoDto {
    private Long id;
    private String cabecalho;
    private Long cursoId;
    private Long aulaId;
    private Dificuldade dificuldade;
    private int xpRecompensa = 10;
}
