package com.machado0.gamificacao.materia.modulo;

import com.machado0.gamificacao.materia.Materia;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "MateriaModulo")
@Table(name = "modulos")
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    private String descricao;

    private int ordem;

    private String titulo;

}
