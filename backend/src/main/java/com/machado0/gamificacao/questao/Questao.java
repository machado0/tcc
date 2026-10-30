package com.machado0.gamificacao.questao;

import com.machado0.gamificacao.aula.Aula;
import com.machado0.gamificacao.curso.Curso;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "questoes")
public class Questao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cabecalho;
    @Enumerated(EnumType.STRING)
    private Dificuldade dificuldade = Dificuldade.FACIL;
    @Column(name = "xp_recompensa", nullable = false)
    private int xpRecompensa = 10;
    @ManyToOne @JoinColumn(name = "curso_id")
    private Curso curso;
    @ManyToOne @JoinColumn(name = "aula_id")
    private Aula aula;
}
