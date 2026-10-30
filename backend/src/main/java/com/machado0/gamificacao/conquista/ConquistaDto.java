package com.machado0.gamificacao.conquista;

import lombok.Data;

@Data
public class ConquistaDto {
    private Long id;
    private String nome;
    private String titulo;
    private String descricao;
    private String icone;
    private int meta;
    private int recompensaXp;
    private Long moduloId;
    private TipoConquista tipo;
}
