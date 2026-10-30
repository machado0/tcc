package com.machado0.gamificacao.turma;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "turmas")
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(name = "ano_letivo", nullable = false)
    private int anoLetivo = 2026;
    @Column(nullable = false)
    private boolean ativo = true;
}
