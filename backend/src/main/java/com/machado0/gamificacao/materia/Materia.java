package com.machado0.gamificacao.materia;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "materias")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    private String icone;

    private String cor;

    @Column(name = "ativo")
    private boolean isAtivo;

    private int ordem;

}
