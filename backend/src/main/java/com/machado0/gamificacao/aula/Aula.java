package com.machado0.gamificacao.aula;

import com.machado0.gamificacao.modulo.Modulo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "aulas")
public class Aula {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "modulo_id")
    private Modulo modulo;
    private String nome;
    private String descricao;
    @Column(name = "duracao_minutos")
    private int duracaoMinutos;
    private int ordem;
}
