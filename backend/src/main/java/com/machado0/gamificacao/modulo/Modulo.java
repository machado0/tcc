package com.machado0.gamificacao.modulo;

import com.machado0.gamificacao.curso.Curso;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "modulos")
public class Modulo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "curso_id")
    private Curso curso;
    private String nome;
    private String descricao;
    private int ordem;
}
