package com.machado0.gamificacao.curso;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cursos")
public class Curso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(nullable = false, length = 250)
    private String descricao;
    private String icone;
}
