package com.machado0.gamificacao.materia.aula;

import com.machado0.gamificacao.materia.modulo.Modulo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "MateriaAula")
@Table(name = "aulas")
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    private int ordem;

    private String titulo;

    private String descricao;

    @Column(name = "duracao_minutos")
    private int duracaoMinutos;

    private int recompensa;

    private String conteudo;

    @Column(name = "pontos_chave")
    private String pontosChave;

    @Column(name = "dica_professor")
    private String dicaProfessor;

}
